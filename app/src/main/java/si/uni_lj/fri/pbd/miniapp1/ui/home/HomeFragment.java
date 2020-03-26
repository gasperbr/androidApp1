package si.uni_lj.fri.pbd.miniapp1.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import si.uni_lj.fri.pbd.miniapp1.MainActivity;
import si.uni_lj.fri.pbd.miniapp1.R;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    Button permissionButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // default home view model logic, not used

        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
            }
        });

        // permissions button
        permissionButton = root.findViewById(R.id.permissionsButton);
        permissionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { askForPermission(); }
        });

        setButtonVisibility();

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        setButtonVisibility();
    }

    public void askForPermission() {
        ((MainActivity)getActivity()).requestPermission();
    }

    public void setButtonVisibility() {
        if (((MainActivity)getActivity()).hasPermission()) {
            permissionButton.setVisibility(View.GONE);
        }
    }

}
