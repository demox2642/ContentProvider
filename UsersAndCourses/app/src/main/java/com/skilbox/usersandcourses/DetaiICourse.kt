package com.skilbox.usersandcourses

import android.app.AlertDialog
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import com.skilbox.usersandcourses.base.Course
import com.skilbox.usersandcourses.base.UserAndCursesVM
import kotlinx.android.synthetic.main.course_list_item.*
import kotlinx.android.synthetic.main.course_list_item.course_id_text
import kotlinx.android.synthetic.main.course_list_item.course_text_edit
import kotlinx.android.synthetic.main.fragment_detail_course.*
import kotlinx.android.synthetic.main.fragment_main.*

class DetaiICourse : Fragment(R.layout.fragment_detail_course) {

    private val viewModel: UserAndCursesVM by navGraphViewModels(R.id.app_nav) {
        defaultViewModelProviderFactory
    }

    private val args: DetaiICourseArgs by navArgs()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.getCourse(args.id)
        observViewModelState()

        change_course.setOnClickListener {
            changeCourse()
        }
    }

    private fun changeCourse() {
        val input = EditText(requireContext())
        input.setHint("Enter Text")
        input.inputType = InputType.TYPE_CLASS_TEXT

        AlertDialog.Builder(requireContext())
            .setTitle("Add Course")
            .setView(input)
            .setNegativeButton("Cancel") { dialog, which ->
            }
            .setPositiveButton("Ok") { dialog, which ->
                viewModel.updateCourse(args.id, input.text.toString())
                viewModel.getCourse(args.id)
            }
            .show()
    }

    private fun observViewModelState() {
        viewModel.courseLD.observe(viewLifecycleOwner) { course -> updateInfo(course) }
        viewModel.loadingLD.observe(viewLifecycleOwner) { loading -> loadingFun(loading) }
    }

    private fun loadingFun(loading: Boolean) {
        if (loading) {
            detail_group.visibility = View.INVISIBLE
            progressBar_detail.visibility = View.VISIBLE
        } else {
            progressBar_detail.visibility = View.INVISIBLE
            detail_group.visibility = View.VISIBLE
        }
    }

    private fun updateInfo(course: Course) {
        course_id_text.text = course.id.toString()
        course_text_edit.text = course.title
    }
}
