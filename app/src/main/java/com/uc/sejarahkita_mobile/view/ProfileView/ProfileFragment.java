package com.uc.sejarahkita_mobile.view.ProfileView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.uc.sejarahkita_mobile.R;
import com.uc.sejarahkita_mobile.helper.SharedPreferenceHelper;
import com.uc.sejarahkita_mobile.model.Profile;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {
    Button btn_logout_profile_fragment;
    LinearLayout linearLayout_playing_history_profile_fragment;
    TextView lbl_register_since_profile_fragment, lbl_username_profile_fragment, lbl_name_profile_fragment, lbl_email_profile_fragment,
            lbl_school_profile_fragment, lbl_city_profile_fragment, lbl_birthyear_profile_fragment,
            lbl_easy_ranked_point_profile_fragment, lbl_hard_ranked_point_profile_fragment;

    private ProfileViewModel profileViewModel;
    private SharedPreferenceHelper helper;
    private static final String TAG = "ProfileFragment";

    public ProfileFragment() {
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        helper = SharedPreferenceHelper.getInstance(requireActivity());
        profileViewModel = new ViewModelProvider(getActivity()).get(ProfileViewModel.class);
        profileViewModel.init(helper.getAccessToken());

        linearLayout_playing_history_profile_fragment = view.findViewById(R.id.linearLayout_playing_history_profile_fragment);
        lbl_register_since_profile_fragment = view.findViewById(R.id.lbl_register_since_profile_fragment);
        lbl_username_profile_fragment = view.findViewById(R.id.lbl_username_profile_fragment);
        lbl_name_profile_fragment = view.findViewById(R.id.lbl_name_profile_fragment);
        lbl_email_profile_fragment = view.findViewById(R.id.lbl_email_profile_fragment);
        lbl_school_profile_fragment = view.findViewById(R.id.lbl_school_profile_fragment);
        lbl_city_profile_fragment = view.findViewById(R.id.lbl_city_profile_fragment);
        lbl_birthyear_profile_fragment = view.findViewById(R.id.lbl_birthyear_profile_fragment);
        lbl_easy_ranked_point_profile_fragment = view.findViewById(R.id.lbl_easy_ranked_point_profile_fragment);
        lbl_hard_ranked_point_profile_fragment = view.findViewById(R.id.lbl_hard_ranked_point_profile_fragment);
        btn_logout_profile_fragment = view.findViewById(R.id.btn_logout_profile_fragment);

        profileViewModel.getProfile();
        profileViewModel.getResultProfiles().observe(getActivity(), showProfile);

//        profileViewModel.getCurrentUser().observe(getViewLifecycleOwner(), new Observer<Profile.Students>() {
//            @Override
//            public void onChanged(Profile.Students students) {
//                lbl_username_profile_fragment.setText(students.getUsername());
//                lbl_name_profile_fragment.setText(students.getName());
//                lbl_email_profile_fragment.setText(students.getEmail());
//                lbl_school_profile_fragment.setText(students.getSchool());
//                lbl_city_profile_fragment.setText(students.getCity());
//                String registerSince = "Register: " + students.getCreated_at();
//                lbl_register_since_profile_fragment.setText(registerSince);
//            }
//        });

        linearLayout_playing_history_profile_fragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavDirections action = ProfileFragmentDirections.actionProfileFragmentToPlayingHistoryFragment();
                Navigation.findNavController(view).navigate(action);
            }
        });

        btn_logout_profile_fragment.setOnClickListener(view1 -> {
            profileViewModel.logout().observe(requireActivity(), s -> {
                if (!s.isEmpty()) {
                    helper.clearPref();
                    NavDirections actions = ProfileFragmentDirections.actionProfileFragmentToLoginFragment();
                    Navigation.findNavController(view1).navigate(actions);
                    Toast.makeText(requireActivity(), s, Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private Observer<Profile> showProfile = new Observer<Profile>() {
        @Override
        public void onChanged(Profile profile) {
            // saat login user Johndoe tapi yang muncul Vanness, masih ada kesalahan.
            Profile.Students results = profile.getStudents().get(0);
            lbl_username_profile_fragment.setText(results.getUsername());
            lbl_name_profile_fragment.setText(results.getName());
            lbl_email_profile_fragment.setText(results.getEmail());
            lbl_school_profile_fragment.setText(results.getSchool());
            lbl_city_profile_fragment.setText(results.getCity());
            lbl_birthyear_profile_fragment.setText(String.valueOf(results.getBirthyear()));
            String registerSince = "Register: " + results.getCreated_at();
            lbl_register_since_profile_fragment.setText(registerSince);
        }
    };

    @Override
    public void onDetach() {
        super.onDetach();
        getActivity().getViewModelStore().clear();
    }
}