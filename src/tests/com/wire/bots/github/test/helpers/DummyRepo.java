package com.wire.bots.github.test.helpers;

import com.wire.bots.sdk.ClientRepo;
import com.wire.bots.sdk.WireClient;
import com.wire.bots.sdk.assets.IAsset;
import com.wire.bots.sdk.assets.IGeneric;
import com.wire.bots.sdk.models.AssetKey;
import com.wire.bots.sdk.models.otr.PreKey;
import com.wire.bots.sdk.server.model.Conversation;
import com.wire.bots.sdk.server.model.User;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class DummyRepo extends ClientRepo {
    public DummyRepo() {
        super(null, null);
    }

    @Override
    public WireClient getWireClient(String botId) {
        return new WireClient() {
            @Override
            public void close() throws IOException {

            }

            @Override
            public void sendText(String txt) throws Exception {

            }

            @Override
            public void sendDirectText(String txt, String userId) throws Exception {

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
            public void sendDirectPicture(byte[] bytes, String mimeType, String userId) throws Exception {

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
            public User getUser(String userId) throws IOException {
                return null;
            }

            @Override
            public Conversation getConversation() throws IOException {
                return null;
            }

            @Override
            public void acceptConnection(String user) throws Exception {

            }

            @Override
            public String decrypt(String userId, String clientId, String cypher) throws Exception {
                return null;
            }

            @Override
            public PreKey newLastPreKey() throws Exception {
                return null;
            }

            @Override
            public ArrayList<PreKey> newPreKeys(int from, int count) throws Exception {
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
            public byte[] downloadProfilePicture(String assetKey) throws Exception {
                return new byte[0];
            }

            @Override
            public AssetKey uploadAsset(IAsset asset) throws Exception {
                return null;
            }

            @Override
            public void call(String content) throws Exception {

            }
        };
    }
}
