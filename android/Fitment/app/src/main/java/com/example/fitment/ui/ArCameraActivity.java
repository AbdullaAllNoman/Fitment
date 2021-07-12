package com.example.fitment.ui;


import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.Toast;

import com.example.fitment.R;
import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.assets.RenderableSource;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ArCameraActivity extends AppCompatActivity
{

    private static final double MIN_OPENGL_VERSION = 3.0;
    private ArFragment arFragment;
    private ModelRenderable modelRenderable;

    @Override
    // CompletableFuture requires api level 24
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if (!checkIsSupportedDeviceOrFinish(this))
        {
            return;
        }
        setContentView(R.layout.activity_ar_camera);
        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment);

        Bundle bundle = getIntent().getExtras();
        String model = bundle.getString("Model"); //receive the renderable model from previous activity
        buildRenderableModelFromSource(model); //render model


        arFragment.setOnTapArPlaneListener(
                (HitResult hitResult, Plane plane, MotionEvent motionEvent) -> {
                    if (modelRenderable == null)
                        return;

                    attachModelToAnchor(hitResult);
                });

    }

    public static boolean checkIsSupportedDeviceOrFinish(final Activity activity)
    {
        // This checks for the android version of the device the app is running on
        // Uncomment this if the minimum required android version is lower than Android 7.0
        // as augmented reality features does not support on those devices
        // Checks the openGL version
        String openGlVersionString =
                ((ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE))
                        .getDeviceConfigurationInfo()
                        .getGlEsVersion();
        if (Double.parseDouble(openGlVersionString) < MIN_OPENGL_VERSION) {
            Toast.makeText(activity, "Sceneform requires OpenGL ES 3.0 or later", Toast.LENGTH_LONG)
                    .show();
            activity.finish();
            return false;
        }
        return true;
    }
    public void buildRenderableModelFromSource (String asset)
    {
        // Returns a CompletableFuture, means the object is being built on a separate thread
        ModelRenderable.builder()
                .setSource(this, RenderableSource.builder().setSource(
                        this, Uri.parse(asset), RenderableSource.SourceType.GLB)
                        .setRecenterMode(RenderableSource.RecenterMode.ROOT)
                        .build())
                .setRegistryId(asset)
                .build()
                .thenAccept(renderable -> modelRenderable = renderable)
                .exceptionally(
                        throwable -> {
                            Toast toast =
                                    Toast.makeText(this, "Unable to load renderable", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            return null;
                        });
    }

    public void attachModelToAnchor(HitResult hitResult)
    {
        // Create the Anchor.
        Anchor anchor = hitResult.createAnchor();
        AnchorNode anchorNode = new AnchorNode(anchor);
        anchorNode.setParent(arFragment.getArSceneView().getScene());

        // Create the transformable model and add it to the anchor.
        TransformableNode transformableNode = new TransformableNode(arFragment.getTransformationSystem());
        transformableNode.getScaleController().setMinScale(0.25f);
        transformableNode.getScaleController().setMaxScale(0.5f);
        transformableNode.setParent(anchorNode);
        transformableNode.setRenderable(modelRenderable);
        transformableNode.select();
    }

}
