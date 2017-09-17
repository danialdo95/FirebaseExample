package com.infideap.firebaseexample;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.infideap.firebaseexample.entity.Person;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class PersonFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private List<Person> persons;
    private MyPersonRecyclerViewAdapter adapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PersonFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static PersonFragment newInstance(int columnCount) {
        PersonFragment fragment = new PersonFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_person_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            persons = new ArrayList<>();

            adapter = new MyPersonRecyclerViewAdapter(persons, mListener);
            recyclerView.setAdapter(adapter);
        }

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        retriveData(view);
    }

    private void retriveData(View view) {
        FirebaseDatabase.getInstance().getReference("persons")
                //in firebase have three data pull listener :
                // 1)addChildEventListener - to one by one data lets save the data is an array
                //                           and the record too huge
                //                           so, the data need to retrieve one by one
                // 2)addValueEventListener - get all data in one shot,
                //                           and will notify if the data is changed
                // 3)addListenerForSingleValueEvent - get all data in one shot, and
                //                                      no update if the data is changed
                .addChildEventListener(new ChildEventListener() {

                    double totalSalary = 0;
                    int totalCars = 0;

                    @Override // this method will trigger if there a new data inserted
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Person person = dataSnapshot.getValue(Person.class);
                        person.key = dataSnapshot.getKey();
                        persons.add(person);
                        adapter.notifyItemInserted(persons.size() - 1);
                        totalSalary += person.salary;
                        totalCars += person.noOfCars;

                        mListener.onTotalUpdateFragmentInteraction(totalSalary, totalCars);
                    }

                    @Override// this method will trigger if there any data been updated
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        Person person = dataSnapshot.getValue(Person.class);
                        person.key = dataSnapshot.getKey();
                        int index = find(persons, person);
                        if (index > -1) {
                            Person person1 = persons.get(index);
                            adapter.notifyItemChanged(index);
                            totalSalary += person.salary - person1.salary;
                            totalCars += person.noOfCars - person1.noOfCars;
                            person1.salary = person.salary;
                            person1.noOfCars = person.noOfCars;

                            mListener.onTotalUpdateFragmentInteraction(totalSalary, totalCars);

                        }
                    }

                    @Override // this method will trigger if there a data been removed
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        Person person = dataSnapshot.getValue(Person.class);
                        person.key = dataSnapshot.getKey();
                        int index = find(persons, person);
                        if (index > -1) {
                            persons.remove(index);
                            adapter.notifyItemRemoved(index);
                            totalSalary -= person.salary;
                            totalCars -= person.noOfCars;

                            mListener.onTotalUpdateFragmentInteraction(totalSalary, totalCars);

                        }
                    }

                    @Override// this method will trigger if the data's position changed
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private int find(List<Person> persons, Person person) {
        int index = 0;
        if (person.key != null)
            for (Person person1 : persons) {
                Log.e("DEBUG", person.key + ", " + person1.key);
                if (person.key.equals(person1.key))
                    return index;

                index++;
            }

        return -1;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Person item);

        void onTotalUpdateFragmentInteraction(double totalSalary, int totalCars);
    }
}
