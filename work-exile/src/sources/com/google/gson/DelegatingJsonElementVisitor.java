package com.google.gson;

import com.google.gson.internal.C$Gson$Preconditions;
import java.io.IOException;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
final class DelegatingJsonElementVisitor implements JsonElementVisitor {
    private final JsonElementVisitor delegate;

    protected DelegatingJsonElementVisitor(JsonElementVisitor delegate) {
        this.delegate = (JsonElementVisitor) C$Gson$Preconditions.checkNotNull(delegate);
    }

    @Override // com.google.gson.JsonElementVisitor
    public void endArray(JsonArray array) throws IOException {
        this.delegate.endArray(array);
    }

    @Override // com.google.gson.JsonElementVisitor
    public void endObject(JsonObject object) throws IOException {
        this.delegate.endObject(object);
    }

    @Override // com.google.gson.JsonElementVisitor
    public void startArray(JsonArray array) throws IOException {
        this.delegate.startArray(array);
    }

    @Override // com.google.gson.JsonElementVisitor
    public void startObject(JsonObject object) throws IOException {
        this.delegate.startObject(object);
    }

    @Override // com.google.gson.JsonElementVisitor
    public void visitArrayMember(JsonArray parent, JsonPrimitive member, boolean isFirst) throws IOException {
        this.delegate.visitArrayMember(parent, member, isFirst);
    }

    @Override // com.google.gson.JsonElementVisitor
    public void visitArrayMember(JsonArray parent, JsonArray member, boolean isFirst) throws IOException {
        this.delegate.visitArrayMember(parent, member, isFirst);
    }

    @Override // com.google.gson.JsonElementVisitor
    public void visitArrayMember(JsonArray parent, JsonObject member, boolean isFirst) throws IOException {
        this.delegate.visitArrayMember(parent, member, isFirst);
    }

    @Override // com.google.gson.JsonElementVisitor
    public void visitObjectMember(JsonObject parent, String memberName, JsonPrimitive member, boolean isFirst) throws IOException {
        this.delegate.visitObjectMember(parent, memberName, member, isFirst);
    }

    @Override // com.google.gson.JsonElementVisitor
    public void visitObjectMember(JsonObject parent, String memberName, JsonArray member, boolean isFirst) throws IOException {
        this.delegate.visitObjectMember(parent, memberName, member, isFirst);
    }

    @Override // com.google.gson.JsonElementVisitor
    public void visitObjectMember(JsonObject parent, String memberName, JsonObject member, boolean isFirst) throws IOException {
        this.delegate.visitObjectMember(parent, memberName, member, isFirst);
    }

    @Override // com.google.gson.JsonElementVisitor
    public void visitNullObjectMember(JsonObject parent, String memberName, boolean isFirst) throws IOException {
        this.delegate.visitNullObjectMember(parent, memberName, isFirst);
    }

    @Override // com.google.gson.JsonElementVisitor
    public void visitPrimitive(JsonPrimitive primitive) throws IOException {
        this.delegate.visitPrimitive(primitive);
    }

    @Override // com.google.gson.JsonElementVisitor
    public void visitNull() throws IOException {
        this.delegate.visitNull();
    }

    @Override // com.google.gson.JsonElementVisitor
    public void visitNullArrayMember(JsonArray parent, boolean isFirst) throws IOException {
        this.delegate.visitNullArrayMember(parent, isFirst);
    }
}
