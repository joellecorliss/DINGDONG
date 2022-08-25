package com.wordle.dingdong

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = navigationArgs.id
        if (id > 0) {
            viewModel.retrieveTask(id).observe(this.viewLifecycleOwner) { selectedTask ->
                task = selectedTask
                bindTask(task)
            }
            binding.deleteBtn.visibility = View.VISIBLE
            binding.deleteBtn.setOnClickListener {
                deleteTask(task)
            }
        } else {
            var getDate: Long = 0
            val datePicker = MaterialDatePicker.Builder.datePicker().setTheme(R.style.MaterialCalendarTheme).build()
            binding.newTaskDateLayout.setEndIconOnClickListener {
                datePicker.apply {
                    show(this@AddTaskDialogFragment.requireActivity().supportFragmentManager, "Date_Picker")
                    addOnPositiveButtonClickListener {
                        getDate = it
                        binding.newTaskDateInput.setText(this.headerText)
                    }
                }
            }
            binding.saveBtn.setOnClickListener {
                addTask(getDate)
            }
        }
    }

    private fun deleteTask(task: Task) {
        viewModel.deleteTask(task)
        findNavController().navigate(
            R.id.action_addTaskDialogFragment_to_taskListFragment
        )
    }

    private fun addTask(date: Long) {
        if (isValidEntry()) {
            viewModel.addTask(
                binding.newTaskTitleInput.text.toString(),
                binding.newTaskDescriptionInput.text.toString(),
                date
            )
            findNavController().navigate(
                R.id.action_addTaskDialogFragment_to_taskListFragment
            )
        }
    }

    private fun updateTask(date: Long) {
        if (isValidEntry()) {
            viewModel.updateTask(
                id = navigationArgs.id,
                name = binding.newTaskTitleInput.text.toString(),
                description = binding.newTaskDescriptionInput.text.toString(),
                date = date
            )
            findNavController().navigate(
                R.id.action_addTaskDialogFragment_to_taskListFragment
            )
        }
    }

    private fun bindTask(task: Task) {
        var getDate: Long = 0
        val datePicker = MaterialDatePicker.Builder.datePicker().setTheme(R.style.MaterialCalendarTheme).build()

        binding.newTaskDateLayout.setEndIconOnClickListener {
            datePicker.apply {
                show(this@AddTaskDialogFragment.requireActivity().supportFragmentManager, "Date_Picker")
                addOnPositiveButtonClickListener {
                    getDate = it
                    binding.newTaskDateInput.setText(this.headerText)
                }
            }
        }

        binding.apply {
            newTaskTitleInput.setText(task.taskTitle, TextView.BufferType.SPANNABLE)
            newTaskDescriptionInput.setText(task.taskDescription, TextView.BufferType.SPANNABLE)
            newTaskDateInput.setText(task.getFullDateString(), TextView.BufferType.SPANNABLE)
            saveBtn.setOnClickListener {
                if (getDate > 0) {
                    updateTask(getDate)
                } else {
                    updateTask(task.taskDate)
                }
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
