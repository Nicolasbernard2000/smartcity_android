package com.example.smartcity_app.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.smartcity_app.R;
import com.example.smartcity_app.model.Report;
import com.example.smartcity_app.model.ReportType;
import com.example.smartcity_app.ui.MainActivity;
import com.example.smartcity_app.viewModels.ReportViewModel;

import java.util.Date;

public class ReportCreationFragment extends Fragment {
    private Button createReportButton;
    private ReportViewModel viewModel;
    private static Context context;
    private static String message;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.report_creation_fragment, container, false);
        this.context = getContext();

        if(!MainActivity.isUserConnected()) {
            Log.d("Debug", "User pas connecté, affichage autre page");

            Bundle bundle = new Bundle();
            bundle.putString("information", getString(R.string.asking_connection));
            Navigation.findNavController(container).navigate(R.id.action_fragment_add_report_to_fragment_information, bundle);
        } else {
            createReportButton = (Button) root.findViewById(R.id.create_report_button);
            createReportButton.setOnClickListener(new CreateReportListener());
            viewModel = new ViewModelProvider(this).get(ReportViewModel.class);
        }

        return root;
    }

    public void MakeToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG);
    }

    private class CreateReportListener implements View.OnClickListener {
        public CreateReportListener() {}
        @Override
        public void onClick(View v) {
            //GET INFO FROM FORM
            viewModel.postReportOnWeb(new Report("Description", "State", "City", "Street", 5500, 13, new Date(2000, 12, 7), 1, new ReportType(1, "label")));
        }
    }



    public static void setToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
