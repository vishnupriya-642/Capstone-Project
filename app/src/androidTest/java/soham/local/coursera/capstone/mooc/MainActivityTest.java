package soham.local.coursera.capstone.mooc;

import android.view.View;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.IsNot.not;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityScenarioRule<MainActivity> scenarioRule = new ActivityScenarioRule<>(MainActivity.class);

    private View decorView;

    @Before
    public void setUp() {
        scenarioRule.getScenario().onActivity(activity -> decorView = activity.getWindow().getDecorView());
    }

    @Test
    public void offlineGenerateQr() {
        onView(withId(R.id.input_text)).perform(typeText("Soham"));
        closeSoftKeyboard();
        onView(withId(R.id.generate_button)).perform(click());

        try {
            Thread.sleep(2500);
            onView(withId(R.id.qr_image_view)).check(matches(ImageViewHasDrawableMatcher.notHasDrawable()));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void onlineGenerateQr() {
        onView(withId(R.id.input_text)).perform(typeText("Soham"));
        closeSoftKeyboard();
        onView(withId(R.id.generate_button)).perform(click());

        try {
            Thread.sleep(2500);
            onView(withId(R.id.qr_image_view)).check(matches(isDisplayed()));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void offlineCacheGenerateQr(){
        onlineGenerateQr();
    }

}
