package org.cryptonews.main;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.cryptonews.main.ui.activities.MyApp;
import org.cryptonews.main.ui.dialogs.ShowDialog;
import org.cryptonews.main.ui.list_utils.Post;
import org.cryptonews.main.ui.list_utils.adapters.PostAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class PrognoseFragment extends Fragment {

    private DatabaseReference reference;
    private PostAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View root = inflater.inflate(R.layout.fragment_prognose,container,false);
       setHasOptionsMenu(true);
       boolean show = getContext().getSharedPreferences(MyApp.prefs, Context.MODE_PRIVATE).getBoolean(MyApp.dialog,true);
       if(show) {
           DialogFragment fragment = new ShowDialog();
           fragment.show(getChildFragmentManager(),"TAG");
       }
       RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.recycler);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),DividerItemDecoration.VERTICAL));
        reference = FirebaseDatabase.getInstance().getReference();
        List<Post> list = new ArrayList<>();
        reference.child("prognose").get().addOnCompleteListener(task -> {
            task.getResult().getChildren().forEach(dataSnapshot -> {
                Post post = dataSnapshot.getValue(Post.class);
                list.add(post);
            });
            Log.d("TAG",list.size()+"");
            Collections.sort(list, (post, t1) -> Utils.convertDate(t1.getDate())-Utils.convertDate(post.getDate()));
            adapter = new PostAdapter(list);
           recyclerView.setAdapter(adapter);
        });
       return root;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
    }
}