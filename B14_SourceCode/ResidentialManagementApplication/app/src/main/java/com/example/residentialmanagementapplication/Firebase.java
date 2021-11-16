package com.example.residentialmanagementapplication;

import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;

interface MyCallback {
    void returnData(ArrayList<Map<String, Object>> docList);
}

public class Firebase {
    private ArrayList<Map<String, Object>> docList = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void addData(String collectionName, Map<String, Object> field) {
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String docID = null;

        for (Map.Entry<String, Object> m : field.entrySet()) {
            if (m.getKey().equalsIgnoreCase("id")) {
                docID = m.getValue().toString();
            }
        }

        if (docID == null) {
            db.collection(collectionName).add(field)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d("TAG", "DocumentSnapshot added with ID: " + documentReference.getId());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("firebase", "Error adding document", e);
                        }
                    });
        } else {
            db.collection(collectionName).document(docID).set(field)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d("firebase", "DocumentSnapshot added with ID: ");
                            } else {
                                Log.w("firebase", "Error adding document");
                            }
                        }
                    });
        }
    }

    public void updData(String collectionName, Map<String, Object> field, String docID) {
//        FirebaseFirestore db = FirebaseFirestore.getInstance();

        if (docID != null) {
            db.collection(collectionName).document(docID).update(field).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Log.d("firebase", "Data update success");
                    } else {
                        Log.d("firebase", "Data update failure");
                    }
                }
            });
        } else {
            Log.d("firebase", "Document ID not found");
        }
    }

    public void getData(String collectionName, String docID, com.example.residentialmanagementapplication.MyCallback myCallback){
//        docList.clear();
        if(docID == null){
            db.collection(collectionName).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        QuerySnapshot result = task.getResult();
                        if(!result.isEmpty()){
                            Log.d("firebase result", result.toString());
                            for (QueryDocumentSnapshot document : result) {
                                Map<String,Object> temp = new HashMap<>();
                                temp.put("id",document.getId());
                                temp.putAll(document.getData());
                                docList.add(temp);
//                                Log.d("firebase", document.getId() + " => " + document.getData());
                            }
                            myCallback.returnData(docList);
                            Log.d("firebase Document", docList.toString());
                        }
                        else
                        {
                            Log.d("firebase Document","no Data exists");
                        }
                    }
                }
            });
        }
        else
        {
            db.collection(collectionName).document(docID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        DocumentSnapshot ref = task.getResult();
                        if(ref.exists()){
                            docList.add(ref.getData());
                            Log.d("firebase Document",ref.getData().toString());
                            myCallback.returnData(docList);
                        }
                        else
                        {
                            Log.d("firebase Document","no Data exists");
                        }
                    }
                }
            });
        }
    }

}
