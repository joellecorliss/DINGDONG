package com.wordle.dingdong

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.wordle.dingdong.adapter.TaskListAdapter
import com.wordle.dingdong.databinding.FragmentTasksBinding
import com.wordle.dingdong.viewmodel.TaskViewModel
import com.wordle.dingdong.viewmodel.TaskViewModelFactory

/**
 * A fragment to view the list of [Task]s stored in the database.
 * Clicking on a [Task] launches the [TaskDetailFragment].
 * Clicking the [FloatingActionButton] launched the [AddTaskDialogFragment]
 */
class TaskListFragment : Fragment() {
    private val viewModel: TaskViewModel by activityViewModels {
        TaskViewModelFactory(
            (activity?.application as BaseApplication).database.taskDao()
        )
    }

    private var _binding: FragmentTasksBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTasksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = TaskListAdapter { task ->
            val action = TaskListFragmentDirections.actionTaskListFragmentToTaskDetailFragment(task.id)
            findNavController().navigate(action)
        }

        viewModel.allTasks.observe(this.viewLifecycleOwner) { tasks ->
            if (tasks.isEmpty()) {
                binding.taskReminderRecyclerView.visibility = View.GONE
                binding.noReminders.visibility = View.VISIBLE
            } else {
                binding.taskReminderRecyclerView.visibility = View.VISIBLE
                binding.noReminders.visibility = View.GONE
                tasks.let {
                    adapter.submitList(it)
                }
            }
        }

        binding.apply {
            taskReminderRecyclerView.adapter = adapter
            newTaskButton.setOnClickListener {
                val action = TaskListFragmentDirections.actionTaskListFragmentToAddTaskDialogFragment(
                    getString(R.string.add_new_task_title)
                )
                findNavController().navigate(action)
            }
        }
    }
}
