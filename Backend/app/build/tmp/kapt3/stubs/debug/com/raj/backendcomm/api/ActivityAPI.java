package com.raj.backendcomm.api;

import java.lang.System;

@kotlin.Metadata(mv = {1, 4, 2}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\u0005\bf\u0018\u00002\u00020\u0001J+\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u00062\b\b\u0001\u0010\u0002\u001a\u00020\u0007H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\bJ!\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\b\b\u0001\u0010\n\u001a\u00020\u0006H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u000b\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\f"}, d2 = {"Lcom/raj/backendcomm/api/ActivityAPI;", "", "activity", "Lretrofit2/Response;", "Lcom/raj/backendcomm/response/ActivityResponse;", "token", "", "Lcom/raj/backendcomm/model/Activity;", "(Ljava/lang/String;Lcom/raj/backendcomm/model/Activity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getActivity", "_id", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public abstract interface ActivityAPI {
    
    @org.jetbrains.annotations.Nullable()
    @retrofit2.http.POST(value = "activity")
    public abstract java.lang.Object activity(@org.jetbrains.annotations.NotNull()
    @retrofit2.http.Header(value = "Authorization")
    java.lang.String token, @org.jetbrains.annotations.NotNull()
    @retrofit2.http.Body()
    com.raj.backendcomm.model.Activity activity, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.raj.backendcomm.response.ActivityResponse>> p2);
    
    @org.jetbrains.annotations.Nullable()
    @retrofit2.http.GET(value = "activity/{_id}")
    public abstract java.lang.Object getActivity(@org.jetbrains.annotations.NotNull()
    @retrofit2.http.Path(value = "_id")
    java.lang.String _id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.raj.backendcomm.response.ActivityResponse>> p1);
}