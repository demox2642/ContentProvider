package com.skilbox.usersandcourses

import android.app.AlertDialog
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.skilbox.usersandcourses.base.ClickItem
import com.skilbox.usersandcourses.base.UserAndCursesVM
import com.skilbox.usersandcourses.base.adapter.CourseAdapter
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : Fragment(R.layout.fragment_main) {

    private var courseAdapter: CourseAdapter? = null

    private val viewModel: UserAndCursesVM by navGraphViewModels(R.id.app_nav) {
        defaultViewModelProviderFactory
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        observViewModelState()
        initList()

        add_course.setOnClickListener {
            addCourse()
        }

        get_course_list.setOnClickListener {
            viewModel.loadCourseList()
        }

        delete_course_list.setOnClickListener {
            viewModel.cleanCourseList()
        }
    }

    private fun addCourse() {

        val input = EditText(requireContext())
        input.setHint("Enter Text")
        input.inputType = InputType.TYPE_CLASS_TEXT

        AlertDialog.Builder(requireContext())
            .setTitle("Add Course")
            .setView(input)
            .setNegativeButton("Cancel") { dialog, which ->
            }
            .setPositiveButton("Ok") { dialog, which ->
                viewModel.addCourse(input.text.toString())
            }
            .show()
    }

    private fun clickHandler(item: ClickItem) {
        when (item.itLongClick) {
            true -> viewModel.deletecourse(item.id)
            false -> detailInfo(item.id)
        }
    }

    private fun initList() {
        viewModel.loadCourseList()
        courseAdapter = CourseAdapter { ClickCarItem -> clickHandler(ClickCarItem) }

        Log.e("addflap", "initList")

        with(course_list_view) {
            adapter = courseAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    private fun observViewModelState() {
        viewModel.courseLiveDataForFragment
            .observe(viewLifecycleOwner) { courseList -> courseAdapter?.items = courseList }

        viewModel.loadingLD.observe(viewLifecycleOwner) { loading -> loadingFun(loading) }
    }

    private fun loadingFun(loading: Boolean) {
        if (loading) {
            buttonPanel.visibility = View.INVISIBLE
            course_list_view.visibility = View.INVISIBLE
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.INVISIBLE
            buttonPanel.visibility = View.VISIBLE
            course_list_view.visibility = View.VISIBLE
        }
    }

    private fun detailInfo(id: Long) {

        val actoin = MainFragmentDirections.actionMainFragmentToDetaiICourse(id)
        findNavController().navigate(actoin)
    }
}
