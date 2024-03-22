package soham.local.coursera.capstone.mooc;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.test.espresso.matcher.BoundedMatcher;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class ImageViewHasDrawableMatcher {

    public static Matcher<View> hasDrawable() {
        return new BoundedMatcher<View, ImageView>(ImageView.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("has drawable");
            }

            @Override
            protected boolean matchesSafely(ImageView imageView) {
                Drawable drawable = imageView.getDrawable();
                return drawable != null;
            }
        };
    }


    public static Matcher<View> notHasDrawable() {
        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(@NonNull Description description) {
                description.appendText("does not have a drawable");
            }

            @Override
            protected boolean matchesSafely(View view) {
                if (view instanceof ImageView) {
                    ImageView imageView = (ImageView) view;
                    Drawable drawable = imageView.getDrawable();

                    // Check if the ImageView does not have a drawable or has a placeholder drawable
                    return drawable == null ;
                }
                return false;
            }
        };
    }
}
