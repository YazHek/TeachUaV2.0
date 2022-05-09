package com.softserve.teachua.ui.task

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.Navigation
import com.softserve.teachua.MainActivity
import com.softserve.teachua.app.tools.Resource
import com.softserve.teachua.data.dto.TaskDto
import com.softserve.teachua.databinding.ChallengeFragmentBinding
import com.softserve.teachua.ui.challenge.ChallengeFragmentArgs
import com.softserve.teachua.ui.compose.HtmlTextWrapper
import com.softserve.teachua.ui.compose.ImageAndTitleOnIt
import com.softserve.teachua.ui.compose.ResourceWrapper
import com.softserve.teachua.ui.compose.TopBar
import com.softserve.teachua.ui.compose.theme.TeachUaComposeTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.challenge_fragment.view.*

@AndroidEntryPoint
class TaskFragment : Fragment() {

    private var _binding: ChallengeFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ChallengeFragmentBinding.inflate(inflater, container, false)
        val view: View = binding.root
        //(requireActivity() as MainActivity).toolbar.visibility = View.GONE
        var id = 1
        if (arguments != null){
            id = ChallengeFragmentArgs.fromBundle(requireArguments()).id
        }
        val compose = view.compose
        compose.setContent {
            TeachUaComposeTheme{
                TaskComposable(id = id) {
                    Navigation.findNavController(view).navigateUp()
                }
            }
        }

        return view
    }


}

@Composable
fun TaskComposable(
    id: Int,
    onAppBarButtonClicked: () -> Unit
) {
    val taskViewModel = hiltViewModel<TaskViewModel>()
    LaunchedEffect(key1 = true) {
        taskViewModel.load(id)
    }
    val task by taskViewModel.task.collectAsState(Resource.loading())

    Column(Modifier.background(MaterialTheme.colors.background)) {
        TaskPage(task) { taskViewModel.load(id) }
    }

}

@Composable
fun TaskPage(task: Resource<TaskDto>, function: () -> Unit) {
    ResourceWrapper(resource = task, onReloadButtonClick = function) {
        val rememberScrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .padding(5.dp)
                .verticalScroll(rememberScrollState)
        ) {
            ImageAndTitleOnIt(picture = task.data?.picture ?: "", title = task.data?.name ?: "")
            val headerAnnotatedString = task.data?.headerText ?: ""
            HeaderText(string = headerAnnotatedString)

            val annotatedString =  task.data?.description ?: ""
            HtmlTextWrapper(annotatedString)
        }

    }
}

@Composable
fun HeaderText(string: String) {
    Spacer(modifier = Modifier.height(10.dp))
    HtmlTextWrapper(string = string)
    Spacer(modifier = Modifier.height(5.dp))
    Divider(color = MaterialTheme.colors.onBackground, thickness = 1.dp)
    Spacer(modifier = Modifier.height(5.dp))
}