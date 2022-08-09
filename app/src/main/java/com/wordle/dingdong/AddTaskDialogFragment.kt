package com.wordle.dingdong

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.datepicker.MaterialDatePicker

import com.wordle.dingdong.databinding.FragmentAddTaskBinding
import com.wordle.dingdong.model.Task
import com.wordle.dingdong.viewmodel.TaskViewModel
import com.wordle.dingdong.viewmodel.TaskViewModelFactory

class AddTaskDialogFragment : Fragment() {
    private val navigationArgs: AddTaskDialogFragmentArgs by navArgs()

    private var _binding: FragmentAddTaskBinding? = null
    private val binding get() = _binding!!

    private lateinit var task: Task

    private val viewModel: TaskViewModel by activityViewModels {
        TaskViewModelFactory(
            (activity?.application as BaseApplication).database.taskDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = navigationArgs.id
        if (id > 0) {
            // TODO: Observe a task that is retrieved by id, set the task variable,
            //  and call the bindTask method
            viewModel.retrieveTask(id).observe(this.viewLifecycleOwner) { selectedTask ->
                task = selectedTask
                bindTask(task)
            }
            binding.deleteBtn.visibility = View.VISIBLE
            binding.deleteBtn.setOnClickListener {
                deleteTask(task)
            }
        } else {
            binding.saveBtn.setOnClickListener {
                addTask()
            }
        }
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        toolbar = view.findViewById(R.id.toolbar)
//        toolbar.setTitle(R.string.add_new_task_title)
//        toolbar.setNavigationOnClickListener{ dismiss() }
//        toolbar.setOnMenuItemClickListener{
//            addTaskToTaskList()
//            true
//        }
//
//
//
//        val datePicker = MaterialDatePicker.Builder.datePicker().build()
//        val datePickerButton: Button = view.findViewById(R.id.pick_date_button)
//        datePickerButton.setOnClickListener{
//            datePicker.apply {
//                show(this@AddTaskDialogFragment.requireActivity().supportFragmentManager, "Date_Picker")
//                addOnPositiveButtonClickListener {
//                    val date = this.headerText
//                    val selectedDateText: TextView = view.findViewById(R.id.selected_date)
//                    selectedDateText.text = date
//                }
//            }
//
//        }
//    }

    private fun deleteTask(task: Task) {
        viewModel.deleteTask(task)
        findNavController().navigate(
            R.id.action_addTaskDialogFragment_to_taskListFragment
        )
    }

    private fun addTask() {
        if (isValidEntry()) {
            val datePicker = MaterialDatePicker.Builder.datePicker().build()
            viewModel.addTask(
                binding.newTaskTitleInput.text.toString(),
                binding.newTaskDescriptionInput.text.toString(),
            )
            findNavController().navigate(
                R.id.action_addTaskDialogFragment_to_taskListFragment
            )
        }
    }

    private fun updateTask() {
        if (isValidEntry()) {
            viewModel.updateTask(
                id = navigationArgs.id,
                name = binding.newTaskTitleInput.text.toString(),
                description = binding.newTaskDescriptionInput.text.toString()
                //date = binding.newTaskDatetime.text.date
            )
            findNavController().navigate(
                R.id.action_addTaskDialogFragment_to_taskListFragment
            )
        }
    }

    private fun bindTask(task: Task) {
        binding.apply{
            newTaskTitleInput.setText(task.taskTitle, TextView.BufferType.SPANNABLE)
            newTaskDescriptionInput.setText(task.taskDescription, TextView.BufferType.SPANNABLE)
            //newTaskDatetime.text = task.taskDate.toString()
            saveBtn.setOnClickListener {
                updateTask()
            }
        }

    }

    private fun isValidEntry() = viewModel.isValidEntry(
        binding.newTaskTitleInput.text.toString(),
        binding.newTaskDescriptionInput.text.toString()
    )

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}