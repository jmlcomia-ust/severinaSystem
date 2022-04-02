import android.os.Bundle;

import com.example.testois.databinding.ActivityCourierDashboardBinding;

public class CourierDashboardActivity extends CourierDrawerBaseActivity {

    ActivityCourierDashboardBinding activityCourierDashboardBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityCourierDashboardBinding = ActivityCourierDashboardBinding.inflate(getLayoutInflater());
        setContentView(activityCourierDashboardBinding.getRoot());
        allocatedActivityTitle("CourierDashboard");
    }
}