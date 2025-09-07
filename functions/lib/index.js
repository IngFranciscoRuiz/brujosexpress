"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.promoteToAdmin = exports.createStoreOwner = void 0;
const admin = require("firebase-admin");
const https_1 = require("firebase-functions/v2/https");
const v2_1 = require("firebase-functions/v2");
admin.initializeApp();
const db = admin.firestore();
(0, v2_1.setGlobalOptions)({ region: "us-central1" });
exports.createStoreOwner = (0, https_1.onCall)(async (request) => {
    if (!request.auth) {
        throw new https_1.HttpsError("unauthenticated", "Auth required");
    }
    const callerClaims = request.auth.token;
    if (callerClaims["role"] !== "admin") {
        throw new https_1.HttpsError("permission-denied", "Only admin can create stores");
    }
    const { email, storeId, store } = request.data;
    if (!email || !storeId || !store || !store.name) {
        throw new https_1.HttpsError("invalid-argument", "email, storeId and store.name are required");
    }
    let user = null;
    try {
        user = await admin.auth().getUserByEmail(email);
    }
    catch (_e) {
        user = await admin.auth().createUser({ email });
    }
    await admin.auth().setCustomUserClaims(user.uid, { role: "store", storeId });
    await db.collection("stores").doc(storeId).set({
        name: store.name,
        logoUrl: store.logoUrl || null,
        phone: store.phone || null,
        deliveryFeeCents: store.deliveryFeeCents || 0,
        isOpen: !!store.isOpen,
        ownerUid: user.uid,
        ownerEmail: email,
        updatedAt: admin.firestore.FieldValue.serverTimestamp(),
    }, { merge: true });
    const link = await admin.auth().generatePasswordResetLink(email);
    return { ok: true, resetLink: link, storeId };
});
const OWNER_EMAILS = new Set([
    "franciscojavier.ruiz.h@gmail.com",
    "francisco.ruiz.h@hotmail.com"
]);
exports.promoteToAdmin = (0, https_1.onCall)(async (request) => {
    if (!request.auth)
        throw new https_1.HttpsError("unauthenticated", "Auth required");
    const callerEmail = request.auth.token.email || "";
    if (!OWNER_EMAILS.has(callerEmail)) {
        throw new https_1.HttpsError("permission-denied", "Restricted to project owner");
    }
    const targetEmail = request.data?.email || callerEmail;
    const user = await admin.auth().getUserByEmail(targetEmail);
    await admin.auth().setCustomUserClaims(user.uid, { role: "admin" });
    return { ok: true, email: targetEmail };
});
//# sourceMappingURL=index.js.map