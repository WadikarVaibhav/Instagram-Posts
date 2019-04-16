package com.android.instaposts;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.UUID;

public class NewPostFragment extends Fragment implements View.OnClickListener {

    public static final String HASHTAGS_NODE = "hashtags";
    public static final String USERS_NODE = "users";
    public static final String IMAGE_UPLOAD_FAILED_MESSAGE = "Image Upload Failed. Try Again";
    public static final String IMAGE_UPLOADED_SUCCESS_MESSAGE = "Image Uploaded successfully";
    public static final String PICTURES_NODE = "pictures";
    public static final String FILE_DELIMETER = "/";
    public static final String SELECT_PICTURE_MEESAGE = "Select Picture";
    public static final String DATA_KEY = "data";
    public static final String NAME_KEY = "name";
    public static final String EMAIL_KEY = "email";
    public static final String IMAGE = "image/*";
    private AlertDialog alertDialog;
    private ImageView imagePreview;
    private Button uploadImageBtn;
    private Uri selectedImgUri;
    private Bitmap selectedImgBitmap;
    private EditText captionText;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private ProgressBar uploadImageProgress;
    private static final int REQUEST_CODE_CAMERA_UPLOAD = 1;
    private static final int REQUEST_CODE_GALLERY_UPLOAD = 2;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_post_fragment, container, false);
        imagePreview = (ImageView) view.findViewById(R.id.imageDisplay);
        uploadImageBtn = (Button) view.findViewById(R.id.imageUploadBtn);
        captionText = (EditText) view.findViewById(R.id.caption);
        uploadImageProgress = (ProgressBar) view.findViewById(R.id.upload_new_post_progress);
        hideWidgetsInitially();
        uploadImageBtn.setOnClickListener(this);
        return view;
    }

    private void hideWidgetsInitially() {
        captionText.setVisibility(View.INVISIBLE);
        uploadImageBtn.setVisibility(View.INVISIBLE);
        imagePreview.setVisibility(View.INVISIBLE);
        uploadImageProgress.setVisibility(View.INVISIBLE);
    }

    private void imageUploadOptions() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(SELECT_PICTURE_MEESAGE);
        builder.setItems(R.array.upload_options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        clickPictureAndUpload();
                        break;
                    case 1:
                        selectPictureFromGallery();
                        break;
                    default:
                        break;
                }
            }
        });
        if (alertDialog == null) {
            alertDialog = builder.create();
            alertDialog.show();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imageUploadOptions();
    }

    private void clickPictureAndUpload() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, REQUEST_CODE_CAMERA_UPLOAD);
    }

    private void selectPictureFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType(IMAGE);
        startActivityForResult(galleryIntent, REQUEST_CODE_GALLERY_UPLOAD);
    }

    private void showImageRelatedWidgets() {
        imagePreview.setVisibility(View.VISIBLE);
        captionText.setVisibility(View.VISIBLE);
        uploadImageBtn.setVisibility(View.VISIBLE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CAMERA_UPLOAD && resultCode == Activity.RESULT_OK) {
            selectedImgBitmap = (Bitmap) data.getExtras().get(DATA_KEY);
        } else if (requestCode == REQUEST_CODE_GALLERY_UPLOAD && resultCode == Activity.RESULT_OK) {
            selectedImgUri = data.getData();
            if (selectedImgUri != null) {
                showImageRelatedWidgets();
                Picasso.get().load(selectedImgUri).fit().centerCrop().into(imagePreview);
            }
        } else {
            alertDialog = null;
            imageUploadOptions();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageUploadBtn:
                uploadImageProgress.setVisibility(View.VISIBLE);
                uploadImage();
                break;
            default:
                break;
        }
    }

    private void uploadImage() {
        if (selectedImgUri != null) {
            makeWidgetsNonEditable();
            storageReference = FirebaseStorage.getInstance().getReference(PICTURES_NODE);
            databaseReference = FirebaseDatabase.getInstance().getReference(PICTURES_NODE);
            final String filename = UUID.randomUUID().toString();
            StorageReference fileRef = storageReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid() + FILE_DELIMETER + filename);
            fileRef.putFile(selectedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    uploadImageProgress.setVisibility(View.GONE);
                    Toast.makeText(getContext(), IMAGE_UPLOADED_SUCCESS_MESSAGE, Toast.LENGTH_SHORT).show();
                    saveImageMetadata(taskSnapshot, filename);
                    openUserProfile();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), IMAGE_UPLOAD_FAILED_MESSAGE, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            hideWidgetsInitially();
        }
    }

    private void saveImageMetadata(UploadTask.TaskSnapshot taskSnapshot, final String filename) {
        Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
        while (!uri.isComplete()) ;
        final Uri url = uri.getResult();
        DatabaseReference currentUser = FirebaseDatabase.getInstance().getReference(USERS_NODE)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        currentUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = new User(dataSnapshot.child(NAME_KEY).getValue().toString(), dataSnapshot.child("nickname").getValue().toString(),
                        dataSnapshot.child(EMAIL_KEY).getValue().toString(), dataSnapshot.child("id").getValue().toString());

                ImageMetadata imageMetadata = new ImageMetadata(captionText.getText().toString(), url.toString(), user);
                databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(filename).setValue(imageMetadata);
                createHashtag(imageMetadata);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void createHashtag(final ImageMetadata imageMetadata) {
        for (final String tag : imageMetadata.getHashtags()) {
            databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    databaseReference.child(HASHTAGS_NODE).child(tag).setValue(tag);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    private void openUserProfile() {
        FragmentCommunicator fragmentCommunicator = (DashboardActivity) getActivity();
        fragmentCommunicator.showProfile();
    }

    private void makeWidgetsNonEditable() {
        uploadImageProgress.setVisibility(View.VISIBLE);
        imagePreview.setEnabled(false);
        captionText.setEnabled(false);
        uploadImageBtn.setEnabled(false);
    }
}
