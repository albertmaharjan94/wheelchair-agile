package com.raj.backendcomm.ui.activity;

import java.lang.System;

@kotlin.Metadata(mv = {1, 4, 2}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001:\u0001\u0016B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u000f\u001a\u00020\u0010H\u0002J\b\u0010\u0011\u001a\u00020\u0010H\u0002J\u0012\u0010\u0012\u001a\u00020\u00102\b\u0010\u0013\u001a\u0004\u0018\u00010\u0014H\u0014J\b\u0010\u0015\u001a\u00020\u0010H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\bX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0006X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0017"}, d2 = {"Lcom/raj/backendcomm/ui/activity/TrackActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "btnChange", "Landroid/widget/Button;", "etDistance", "Landroid/widget/EditText;", "etLat", "Landroid/widget/TextView;", "etLon", "etSpeed", "locationManager", "Landroid/location/LocationManager;", "userDetail", "Lcom/raj/backendcomm/model/User;", "binding", "", "findLocation", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "updateLocationAPI", "MylocationListener", "app_debug"})
public final class TrackActivity extends androidx.appcompat.app.AppCompatActivity {
    private android.widget.EditText etSpeed;
    private android.widget.EditText etDistance;
    private android.widget.TextView etLat;
    private android.widget.TextView etLon;
    private android.widget.Button btnChange;
    private android.location.LocationManager locationManager;
    private com.raj.backendcomm.model.User userDetail;
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void findLocation() {
    }
    
    private final void updateLocationAPI() {
    }
    
    private final void binding() {
    }
    
    public TrackActivity() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 4, 2}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0086\u0004\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0016J$\u0010\u0007\u001a\u00020\u00042\b\u0010\b\u001a\u0004\u0018\u00010\t2\u0006\u0010\n\u001a\u00020\u000b2\b\u0010\f\u001a\u0004\u0018\u00010\rH\u0016\u00a8\u0006\u000e"}, d2 = {"Lcom/raj/backendcomm/ui/activity/TrackActivity$MylocationListener;", "Landroid/location/LocationListener;", "(Lcom/raj/backendcomm/ui/activity/TrackActivity;)V", "onLocationChanged", "", "location", "Landroid/location/Location;", "onStatusChanged", "p0", "", "p1", "", "p2", "Landroid/os/Bundle;", "app_debug"})
    public final class MylocationListener implements android.location.LocationListener {
        
        @java.lang.Override()
        public void onLocationChanged(@org.jetbrains.annotations.NotNull()
        android.location.Location location) {
        }
        
        @java.lang.Override()
        public void onStatusChanged(@org.jetbrains.annotations.Nullable()
        java.lang.String p0, int p1, @org.jetbrains.annotations.Nullable()
        android.os.Bundle p2) {
        }
        
        public MylocationListener() {
            super();
        }
    }
}