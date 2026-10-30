package com.example.practicalfive;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class MunicipalityFlowTest {
    private final Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
    private final Context context = instrumentation.getTargetContext();

    @Test
    public void requiredValidInputsOpenTheCorrectScreen() {
        String[][] inputs = {
                {"Beijing", "Beijing"}, {"beijing", "Beijing"}, {"BEIJING", "Beijing"},
                {"Shanghai", "Shanghai"}, {"Tianjin", "Tianjin"}, {"Chongqing", "Chongqing"}
        };
        for (String[] input : inputs) {
            Instrumentation.ActivityMonitor monitor = instrumentation.addMonitor(
                    Municipality.class.getName(), null, false);
            try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
                onView(withId(R.id.municipality_input)).perform(replaceText(input[0]), pressImeActionButton());
                Activity detail = monitor.waitForActivityWithTimeout(5000);
                assertNotNull("No detail screen for " + input[0], detail);
                assertEquals(input[1], detail.getIntent().getStringExtra(Municipality.EXTRA_MUNICIPALITY));
                onView(withId(R.id.municipality_name)).check(matches(withText(input[1])));
                instrumentation.runOnMainSync(detail::finish);
            } finally {
                instrumentation.removeMonitor(monitor);
            }
        }
    }

    @Test
    public void invalidInputsShowAnErrorAndStayOnMainScreen() {
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            for (String input : new String[]{"Guangzhou", "", "   "}) {
                onView(withId(R.id.municipality_input)).perform(replaceText(input), pressImeActionButton());
                onView(withId(R.id.municipality_input)).check(matches(
                        hasErrorText(context.getString(R.string.invalid_municipality))));
                onView(withId(R.id.main_title)).check(matches(isDisplayed()));
            }
        }
    }

    @Test
    public void eachCityHasItsOwnSummaryAndImage() {
        String[] names = {"Beijing", "Shanghai", "Chongqing", "Tianjin"};
        int[] images = {R.drawable.beijing, R.drawable.shanghai, R.drawable.chongqing, R.drawable.tianjin};
        String[] summaries = context.getResources().getStringArray(R.array.municipalities_array);
        assertEquals(4, summaries.length);
        for (int index = 0; index < names.length; index++) {
            final int city = index;
            try (ActivityScenario<Municipality> scenario = openCity(names[city])) {
                scenario.onActivity(activity -> {
                    TextView name = activity.findViewById(R.id.municipality_name);
                    TextView information = activity.findViewById(R.id.municipality_information);
                    ImageView image = activity.findViewById(R.id.municipality_image);
                    assertEquals(names[city], name.getText().toString());
                    assertEquals(summaries[city], information.getText().toString());
                    assertTrue(information.getText().toString().startsWith(names[city] + " is"));
                    int words = information.getText().toString().trim().split("\\s+").length;
                    assertTrue("Summary length: " + words, words >= 80 && words <= 120);
                    Drawable expected = activity.getDrawable(images[city]);
                    assertNotNull(expected);
                    assertEquals(expected.getConstantState(), image.getDrawable().getConstantState());
                });
                onView(withId(R.id.visit_site)).check(matches(isDisplayed()));
            }
        }
    }

    @Test
    public void missingOrInvalidIntentDataClosesSafely() {
        for (String name : new String[]{null, "", "   ", "Guangzhou"}) {
            try (ActivityScenario<Municipality> scenario = openCity(name)) {
                assertEquals(Lifecycle.State.DESTROYED, scenario.getState());
            }
        }
    }

    private ActivityScenario<Municipality> openCity(String name) {
        Intent intent = new Intent(context, Municipality.class);
        if (name != null) {
            intent.putExtra(Municipality.EXTRA_MUNICIPALITY, name);
        }
        return ActivityScenario.launch(intent);
    }
}
