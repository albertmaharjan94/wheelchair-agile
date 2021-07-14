package com.raj.backendcomm.api;

import java.lang.System;

@kotlin.Metadata(mv = {1, 4, 2}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u001f\u0010\u0019\u001a\u0002H\u001a\"\u0004\b\u0000\u0010\u001a2\f\u0010\u001b\u001a\b\u0012\u0004\u0012\u0002H\u001a0\u001c\u00a2\u0006\u0002\u0010\u001dR\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u001c\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0007\u0010\b\"\u0004\b\t\u0010\nR\u000e\u0010\u000b\u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u000f\u001a\n \u0011*\u0004\u0018\u00010\u00100\u0010X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u0012\u001a\n \u0011*\u0004\u0018\u00010\u00130\u0013X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001c\u0010\u0014\u001a\u0004\u0018\u00010\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0015\u0010\u0016\"\u0004\b\u0017\u0010\u0018\u00a8\u0006\u001e"}, d2 = {"Lcom/raj/backendcomm/api/ServiceBuilder;", "", "()V", "BaseURL", "", "logged_user", "Lcom/raj/backendcomm/model/User;", "getLogged_user", "()Lcom/raj/backendcomm/model/User;", "setLogged_user", "(Lcom/raj/backendcomm/model/User;)V", "logger", "Lokhttp3/logging/HttpLoggingInterceptor;", "okHttp", "Lokhttp3/OkHttpClient$Builder;", "retrofit", "Lretrofit2/Retrofit;", "kotlin.jvm.PlatformType", "retrofitBuilder", "Lretrofit2/Retrofit$Builder;", "token", "getToken", "()Ljava/lang/String;", "setToken", "(Ljava/lang/String;)V", "buildService", "T", "serviceType", "Ljava/lang/Class;", "(Ljava/lang/Class;)Ljava/lang/Object;", "app_debug"})
public final class ServiceBuilder {
    private static final java.lang.String BaseURL = "http://10.0.2.2:3000/api/";
    @org.jetbrains.annotations.Nullable()
    private static java.lang.String token;
    @org.jetbrains.annotations.Nullable()
    private static com.raj.backendcomm.model.User logged_user;
    private static final okhttp3.logging.HttpLoggingInterceptor logger = null;
    private static final okhttp3.OkHttpClient.Builder okHttp = null;
    private static final retrofit2.Retrofit.Builder retrofitBuilder = null;
    private static final retrofit2.Retrofit retrofit = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.raj.backendcomm.api.ServiceBuilder INSTANCE = null;
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getToken() {
        return null;
    }
    
    public final void setToken(@org.jetbrains.annotations.Nullable()
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.raj.backendcomm.model.User getLogged_user() {
        return null;
    }
    
    public final void setLogged_user(@org.jetbrains.annotations.Nullable()
    com.raj.backendcomm.model.User p0) {
    }
    
    public final <T extends java.lang.Object>T buildService(@org.jetbrains.annotations.NotNull()
    java.lang.Class<T> serviceType) {
        return null;
    }
    
    private ServiceBuilder() {
        super();
    }
}