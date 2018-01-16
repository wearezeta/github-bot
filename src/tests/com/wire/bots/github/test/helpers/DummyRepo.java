package com.wire.bots.github.test.helpers;

import com.wire.bots.sdk.ClientRepo;
import com.wire.bots.sdk.WireClient;
import com.wire.bots.sdk.assets.IAsset;
import com.wire.bots.sdk.assets.IGeneric;
import com.wire.bots.sdk.assets.OT;
import com.wire.bots.sdk.models.AssetKey;
import com.wire.bots.sdk.models.otr.PreKey;
import com.wire.bots.sdk.server.model.Conversation;
import com.wire.bots.sdk.server.model.User;
import com.wire.cryptobox.CryptoException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class DummyRepo extends ClientRepo{
    public DummyRepo() {
        super((botId, conversationId, clientId, token) -> new WireClient() {
            @Override
            public void sendText(String txt) throws Exception {

            }

            @Override
            public void sendText(String txt, long expires) throws Exception {

            }

            @Override
            public void sendText(String txt, long expires, String messageId) throws Exception {

            }

            @Override
            public void sendLinkPreview(String url, String title, IGeneric image) throws Exception {

            }

            @Override
            public void sendPicture(byte[] bytes, String mimeType) throws Exception {

            }

            @Override
            public void sendPicture(IGeneric image) throws Exception {

            }

            @Override
            public void sendAudio(byte[] bytes, String name, String mimeType, long duration) throws Exception {

            }

            @Override
            public void sendVideo(byte[] bytes, String name, String mimeType, long duration, int h, int w) throws Exception {

            }

            @Override
            public void sendFile(File file, String mime) throws Exception {

            }

            @Override
            public void ping() throws Exception {

            }

            @Override
            public void sendOT(OT ot) throws Exception {

            }

            @Override
            public void sendDelivery(String msgId) throws Exception {

            }

            @Override
            public void sendReaction(String msgId, String emoji) throws Exception {

            }

            @Override
            public void deleteMessage(String msgId) throws Exception {

            }

            @Override
            public byte[] downloadAsset(String assetKey, String assetToken, byte[] sha256Challenge, byte[] otrKey) throws Exception {
                return new byte[0];
            }

            @Override
            public String getId() {
                return null;
            }

            @Override
            public String getConversationId() {
                return null;
            }

            @Override
            public String getDeviceId() {
                return null;
            }

            @Override
            public Collection<User> getUsers(Collection<String> userIds) throws IOException {
                return null;
            }

            @Override
            public Conversation getConversation() throws IOException {
                return null;
            }

            @Override
            public void acceptConnection(String user) throws IOException {

            }

            @Override
            public byte[] decrypt(String userId, String clientId, String cypher) throws CryptoException {
                return new byte[0];
            }

            @Override
            public PreKey newLastPreKey() throws CryptoException {
                return null;
            }

            @Override
            public ArrayList<PreKey> newPreKeys(int from, int count) throws CryptoException {
                return null;
            }

            @Override
            public void uploadPreKeys(ArrayList<PreKey> preKeys) throws IOException {

            }

            @Override
            public ArrayList<Integer> getAvailablePrekeys() {
                return null;
            }

            @Override
            public boolean isClosed() {
                return false;
            }

            @Override
            public byte[] downloadProfilePicture(String assetKey) throws IOException {
                return new byte[0];
            }

            @Override
            public AssetKey uploadAsset(IAsset asset) throws Exception {
                return null;
            }

            @Override
            public void close() throws IOException {

            }
        }, null);
    }
}
