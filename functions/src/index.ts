import * as admin from "firebase-admin";
import { onCall, HttpsError } from "firebase-functions/v2/https";
import { setGlobalOptions } from "firebase-functions/v2";

admin.initializeApp();
const db = admin.firestore();

setGlobalOptions({ region: "us-central1" });

export const createStoreOwner = onCall(async (request) => {
  if (!request.auth) {
    throw new HttpsError("unauthenticated", "Auth required");
  }
  const callerClaims = request.auth.token as Record<string, unknown>;
  if (callerClaims["role"] !== "admin") {
    throw new HttpsError("permission-denied", "Only admin can create stores");
  }

  const { email, storeId, store } = request.data as { email: string; storeId: string; store: any };
  if (!email || !storeId || !store || !store.name) {
    throw new HttpsError("invalid-argument", "email, storeId and store.name are required");
  }

  // Create or get user
  let user: admin.auth.UserRecord | null = null;
  try {
    user = await admin.auth().getUserByEmail(email);
  } catch (_e) {
    user = await admin.auth().createUser({ email });
  }

  // Set custom claims
  await admin.auth().setCustomUserClaims(user!.uid, { role: "store", storeId });

  // Create/overwrite store document
  await db.collection("stores").doc(storeId).set(
    {
      name: store.name,
      logoUrl: store.logoUrl || null,
      phone: store.phone || null,
      deliveryFeeCents: store.deliveryFeeCents || 0,
      isOpen: !!store.isOpen,
      ownerUid: user!.uid,
      ownerEmail: email,
      updatedAt: admin.firestore.FieldValue.serverTimestamp(),
    },
    { merge: true }
  );

  // Send password reset link
  const link = await admin.auth().generatePasswordResetLink(email);
  return { ok: true, resetLink: link, storeId };
});

// Bootstrap: promote a user to admin. Restrict by caller email allowlist
const OWNER_EMAILS = new Set<string>([
  "franciscojavier.ruiz.h@gmail.com",
  "francisco.ruiz.h@hotmail.com"
]);

export const promoteToAdmin = onCall(async (request) => {
  if (!request.auth) throw new HttpsError("unauthenticated", "Auth required");
  const callerEmail = (request.auth.token.email as string | undefined) || "";
  if (!OWNER_EMAILS.has(callerEmail)) {
    throw new HttpsError("permission-denied", "Restricted to project owner");
  }
  const targetEmail = (request.data?.email as string | undefined) || callerEmail;
  const user = await admin.auth().getUserByEmail(targetEmail);
  await admin.auth().setCustomUserClaims(user.uid, { role: "admin" });
  return { ok: true, email: targetEmail };
});


