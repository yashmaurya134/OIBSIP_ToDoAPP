package com.example.todolist;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assume.assumeTrue;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.todolist.Model.Task;
import com.example.todolist.Model.AppDatabase;
import com.example.todolist.Model.TaskDao;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Objects;


@LargeTest
@RunWith(AndroidJUnit4.class)
public class HomeFragmentInstrumentedTest {

    AppDatabase db = Room.databaseBuilder(ApplicationProvider.getApplicationContext(),
            AppDatabase.class, "tasks-db").allowMainThreadQueries().build();
    TaskDao taskDao = db.taskDao();

    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);


    @Test
    public void emptyQueryTextViewTest() {
        assumeTrue("Test skipped, database is populated", taskDao.getIncomplete().isEmpty());
        ViewInteraction noTaskTextView = onView(
                allOf(withId(R.id.noTaskTextView),
                        withParent(withParent(withId(R.id.nav_host_fragment_content_main))),
                        isDisplayed()));
        noTaskTextView.check(matches(isDisplayed()));
        noTaskTextView.check(matches(withText("No Tasks")));

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.recyclerView),
                        withParent(withParent(withId(R.id.nav_host_fragment_content_main))),
                        isDisplayed()));
        recyclerView.check(doesNotExist());
    }


    @Test
    public void populatedQueryRecyclerViewTest(){
        assumeTrue("Test skipped, database is empty", !taskDao.getIncomplete().isEmpty());
        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.recyclerView),
                        withParent(withParent(withId(R.id.nav_host_fragment_content_main))),
                        isDisplayed()));
        recyclerView.check(matches(isDisplayed()));
        ViewInteraction relativeLayout = onView(
                withIndex(withId(R.id.task_container), 0));
        relativeLayout.check(matches(isDisplayed()));
    }

    @Test
    public void clickRecyclerViewItemTest(){
        assumeTrue("Test skipped, database is empty", !taskDao.getIncomplete().isEmpty());

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.recyclerView),
                        childAtPosition(
                                withClassName(is("android.widget.RelativeLayout")),
                                0)));
        recyclerView.perform(actionOnItemAtPosition(0, click()));

        ViewInteraction linearLayout = onView(
                allOf(withParent(allOf(withId(android.R.id.content),
                                withParent(IsInstanceOf.<View>instanceOf(android.widget.FrameLayout.class)))),
                        isDisplayed()));
        linearLayout.check(matches(isDisplayed()));

        ViewInteraction bottomSheetHeader = onView(
                allOf(withId(R.id.bottom_sheet_choose), withText("Choose"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        bottomSheetHeader.check(matches(isDisplayed()));

        ViewInteraction bottomSheetMarkComplete = onView(
                allOf(withId(R.id.bottom_sheet_markComplete),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        bottomSheetMarkComplete.check(matches(isDisplayed()));

        ViewInteraction bottomSheetEdit = onView(
                allOf(withId(R.id.bottom_sheet_edit),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        bottomSheetEdit.check(matches(isDisplayed()));

        ViewInteraction bottomSheetDelete = onView(
                allOf(withId(R.id.bottom_sheet_delete),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        bottomSheetDelete.check(matches(isDisplayed()));
    }


    @Test
    public void clickBottomSheetDeleteTest(){
        assumeTrue("Test skipped, database is empty", !taskDao.getIncomplete().isEmpty());

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.recyclerView),
                        childAtPosition(
                                withClassName(is("android.widget.RelativeLayout")),
                                0)));
        recyclerView.perform(actionOnItemAtPosition(0, click()));

        ViewInteraction linearLayout = onView(
                allOf(withId(R.id.bottom_sheet_delete),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        linearLayout.perform(click());

        ViewInteraction linearLayout2 = onView(
                allOf(withParent(allOf(withId(android.R.id.content),
                                withParent(IsInstanceOf.<View>instanceOf(android.widget.FrameLayout.class)))),
                        isDisplayed()));
        linearLayout2.check(matches(isDisplayed()));

        ViewInteraction imageView = onView(
                allOf(withId(R.id.imageView5), withContentDescription("Trashcan"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        imageView.check(matches(isDisplayed()));

        ViewInteraction textView = onView(
                allOf(withId(R.id.delete_dialog_ays), withText("Are You Sure?"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        textView.check(matches(isDisplayed()));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.delete_dialog_msg), withText("Do you really want to delete this task? This process cannot be undone."),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        textView2.check(matches(isDisplayed()));

        ViewInteraction button = onView(
                allOf(withId(R.id.cancel_delete_button),
                        withParent(withParent(IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class))),
                        isDisplayed()));
        button.check(matches(isDisplayed()));

        ViewInteraction button2 = onView(
                allOf(withId(R.id.delete_button),
                        withParent(withParent(IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class))),
                        isDisplayed()));
        button2.check(matches(isDisplayed()));
    }


    @Test
    public void cancelDeleteTest(){
        assumeTrue("Test skipped, database is empty", !taskDao.getIncomplete().isEmpty());

        int startNumOfTasks = taskDao.getIncomplete().size();

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.recyclerView),
                        childAtPosition(
                                withClassName(is("android.widget.RelativeLayout")),
                                0)));
        recyclerView.perform(actionOnItemAtPosition(0, click()));

        ViewInteraction linearLayout = onView(
                allOf(withId(R.id.bottom_sheet_delete),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        linearLayout.perform(click());

        ViewInteraction cancelDeleteButton = onView(
                allOf(withId(R.id.cancel_delete_button), withText("Cancel"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        3),
                                1),
                        isDisplayed()));
        cancelDeleteButton.perform(click());

        ViewInteraction linearLayout2 = onView(
                allOf(withParent(allOf(withId(android.R.id.content),
                                withParent(IsInstanceOf.<View>instanceOf(android.widget.FrameLayout.class)))),
                        isDisplayed()));
        linearLayout2.check(doesNotExist());

        assert Objects.equals(startNumOfTasks, taskDao.getIncomplete().size());
    }


    @Test
    public void confirmDeleteTest(){
        assumeTrue("Test skipped, database is empty", !taskDao.getIncomplete().isEmpty());
        Task firstTask = taskDao.getIncomplete().get(0);
        int startNumOfTasks = taskDao.getIncomplete().size();

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.recyclerView),
                        childAtPosition(
                                withClassName(is("android.widget.RelativeLayout")),
                                0)));
        recyclerView.perform(actionOnItemAtPosition(0, click()));

        ViewInteraction linearLayout = onView(
                allOf(withId(R.id.bottom_sheet_delete),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        linearLayout.perform(click());

        ViewInteraction deleteButton = onView(
                allOf(withId(R.id.delete_button), withText("Delete"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        3),
                                2),
                        isDisplayed()));
        deleteButton.perform(click());

        ViewInteraction linearLayout2 = onView(
                allOf(withParent(allOf(withId(android.R.id.content),
                                withParent(IsInstanceOf.<View>instanceOf(android.widget.FrameLayout.class)))),
                        isDisplayed()));
        linearLayout2.check(doesNotExist());

        assert Objects.equals(startNumOfTasks - 1, taskDao.getIncomplete().size());
        taskDao.insert(firstTask);
    }


    @Test
    public void MarkCompleteTest() {
        assumeTrue("Test skipped, database is empty", !taskDao.getIncomplete().isEmpty());
        int startNumOfTasks = taskDao.getIncomplete().size();

        ViewInteraction relativeLayout = onView(
                allOf(withId(R.id.task_container),
                        childAtPosition(
                                allOf(withId(R.id.card_view),
                                        childAtPosition(
                                                withId(R.id.recyclerView),
                                                0)),
                                0),
                        isDisplayed()));
        relativeLayout.perform(click());


        ViewInteraction linearLayout = onView(
                allOf(withId(R.id.bottom_sheet_markComplete),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        linearLayout.perform(click());

        int afterMarkedComplete = taskDao.getIncomplete().size();
        assert (startNumOfTasks != afterMarkedComplete);

        ViewInteraction materialButton = onView(
                allOf(withId(com.google.android.material.R.id.snackbar_action), withText("Undo"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("com.google.android.material.snackbar.Snackbar$SnackbarLayout")),
                                        0),
                                1),
                        isDisplayed()));
        materialButton.perform(click());

        int afterUndo = taskDao.getIncomplete().size();
        assert (afterUndo == startNumOfTasks);
    }


    @Test
    public void EditTaskTest() {
        assumeTrue("Test skipped, database is empty", !taskDao.getIncomplete().isEmpty());
        ViewInteraction relativeLayout = onView(
                allOf(withId(R.id.task_container),
                        childAtPosition(
                                allOf(withId(R.id.card_view),
                                        childAtPosition(
                                                withId(R.id.recyclerView),
                                                0)),
                                0),
                        isDisplayed()));
        relativeLayout.perform(click());

        ViewInteraction linearLayout = onView(
                allOf(withId(R.id.bottom_sheet_edit),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        linearLayout.perform(click());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.description_task),
                        childAtPosition(
                                allOf(withId(R.id.popup_window),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                5),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("New Description"));

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.description_task), withText("New Description"),
                        childAtPosition(
                                allOf(withId(R.id.popup_window),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                5),
                        isDisplayed()));
        appCompatEditText2.perform(closeSoftKeyboard());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.task_name),
                        childAtPosition(
                                allOf(withId(R.id.popup_window),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatEditText3.perform(replaceText("Edited Test"));

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.task_name),
                        childAtPosition(
                                allOf(withId(R.id.popup_window),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatEditText4.perform(closeSoftKeyboard());

        ViewInteraction materialButton = onView(
                allOf(withId(R.id.update_button), withText("Update"),
                        childAtPosition(
                                allOf(withId(R.id.popup_window),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                7),
                        isDisplayed()));
        materialButton.perform(click());
    }

    private static Matcher<View> withIndex(final Matcher<View> matcher, final int index) {
        return new TypeSafeMatcher<View>() {
            int currentIndex = 0;

            @Override
            public void describeTo(Description description) {
                description.appendText("with index: ");
                description.appendValue(index);
                matcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                return matcher.matches(view) && currentIndex++ == index;
            }
        };
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
