package com.example.newsapp38m4.ui.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.newsapp38m4.ui.news.NewsFragment;
import com.example.newsapp38m4.ui.news.NewsItemModel;
import com.example.newsapp38m4.ui.news.NewsRecyclerAdapter;
import com.example.newsapp38m4.ui.news.OnItemClickListener;
import com.example.newsapp38m4.R;
import com.example.newsapp38m4.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private NewsRecyclerAdapter recyclerAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recyclerAdapter = new NewsRecyclerAdapter();

    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFragment();
            }
        });
        getParentFragmentManager().setFragmentResultListener("rk.news", getViewLifecycleOwner(), new FragmentResultListener() {

            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                NewsItemModel newsItemModel = (NewsItemModel) result.getSerializable("news.content");

                recyclerAdapter.addItem(newsItemModel);
            }
        });
        initList();

        recyclerAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onClick(int position) {
                recyclerAdapter.editItem(position);
                String titleText = recyclerAdapter.getItemString(position);
                Log.e("f_home", "sent String to edit: " + titleText);
//                EditText editText = view.findViewById(R.id.edit_text);

                NewsFragment newsFragment = new NewsFragment();
                Bundle bundle = new Bundle();
//                bundle.putBoolean("news.edit", true);
                bundle.putString("news.edit.title", titleText);
                newsFragment.setArguments(bundle);
                openFragment();
            }

            @Override
            public void onLongClick(int position) {
                NewsItemModel newsItemModel = recyclerAdapter.getItem(position);
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                alert.setTitle("Delete?");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        recyclerAdapter.deleteItem(position);
                        Toast.makeText(getActivity(), "Deleted", Toast.LENGTH_SHORT).show();
                    }
                });
                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity(), "Cancelled", Toast.LENGTH_SHORT).show();
                    }
                });
                alert.show();
            }
        });
    }

    private void initList() {
        binding.rvHome.setAdapter(recyclerAdapter);
    }

    private void openFragment() {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
        navController.navigate(R.id.newsFragment);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}