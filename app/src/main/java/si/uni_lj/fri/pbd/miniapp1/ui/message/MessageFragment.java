package si.uni_lj.fri.pbd.miniapp1.ui.message;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import si.uni_lj.fri.pbd.miniapp1.R;
import si.uni_lj.fri.pbd.miniapp1.ui.home.HomeViewModel;

public class MessageFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_message, container, false);

        return root;
    }
}
