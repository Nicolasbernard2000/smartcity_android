package com.example.smartcity_app.view.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.smartcity_app.R;
import com.example.smartcity_app.util.Constants;
import com.example.smartcity_app.viewModel.AccountViewModel;
import com.google.android.material.tabs.TabLayout;

public class ProfileFragment extends Fragment {
    private TextView information;
    private TabLayout tabLayout;
    private AccountViewModel accountViewModel;
    private String token;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.profile_fragment, container, false);

        information = root.findViewById(R.id.profile_information);
        tabLayout = root.findViewById(R.id.table_layout);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences(Constants.TOKEN, Context.MODE_PRIVATE);
        token = sharedPreferences.getString(Constants.TOKEN, null);

        loadFragment(new ProfileInformationFragment());

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment fragment = null;
                switch (tab.getPosition()) {
                    case 0:
                        fragment = new ProfileInformationFragment();
                        break;
                    case 1:
                        fragment = new ProfilePersonalReportsFragment();
                        break;
                }
                loadFragment(fragment);
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        accountViewModel = new ViewModelProvider(this).get(AccountViewModel.class);
        accountViewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            String info = user.getLastName().toUpperCase() + " " + user.getFirstName();
            information.setText(info);
        });

        if(token != null) {
            accountViewModel.getUserFromToken(token);
        }

    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
    }
}
