package com.example.mobileproject;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import Adapters.CandidateAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link coach_candidates#newInstance} factory method to
 * create an instance of this fragment.
 */
public class coach_candidates extends Fragment {

    private RecyclerView recyclerView;
    private CandidateAdapter candidateAdapter;
    private List<String[]> candidateList;
    private ImageView backButton;

    public coach_candidates() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment coach_candidates.
     */
    public static coach_candidates newInstance(String param1, String param2) {
        coach_candidates fragment = new coach_candidates();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_coach_candidates, container, false);

        recyclerView = view.findViewById(R.id.candidats_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Static data for candidates (no need for a Candidate entity)
        candidateList = new ArrayList<>();
        candidateList.add(new String[]{"John Doe", "Intermediate", "Lose Weight", String.valueOf(R.drawable.profile_image)});
        candidateList.add(new String[]{"Jane Smith", "Beginner", "Build Muscle", String.valueOf(R.drawable.profile_image)});
        candidateList.add(new String[]{"Alice Johnson", "Advanced", "Improve Flexibility", String.valueOf(R.drawable.profile_image)});

        // Set the adapter with the OnItemClickListener
        candidateAdapter = new CandidateAdapter(candidateList, new CandidateAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String[] clickedCandidate = candidateList.get(position);
                String candidateName = clickedCandidate[0]; // Get the candidate's name

                // Create a new instance of PlannerFragment
                PlannerFragment plannerFragment = new PlannerFragment();

                // Pass the candidate name to the PlannerFragment
                Bundle bundle = new Bundle();
                bundle.putString("candidate_name", candidateName); // Store the candidate name
                plannerFragment.setArguments(bundle);

                // Start the PlannerFragment
                getFragmentManager().beginTransaction()
                        .replace(R.id.fragment, plannerFragment)  // R.id.container is the container where fragments are placed
                        .addToBackStack(null)  // Add to the back stack if you want the user to be able to press "Back"
                        .commit();
            }

        });
        recyclerView.setAdapter(candidateAdapter);

        backButton = view.findViewById(R.id.back_button);
        if (backButton != null) {
            backButton.setOnClickListener(v -> goBackToPlanner());
        } else {
            Log.e("MealFormFragment", "Back Button not found.");
        }
        return view;
    }
    private void goBackToPlanner() {
        Log.d("planner fragment", "Going back to coach candidates fragment");
        if (getActivity() != null) {
//            requireActivity().getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.fragment, new PlannerFragment())
//                    .addToBackStack(null)
//                    .commit();
        }
    }
}
