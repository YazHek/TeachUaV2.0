package com.softserve.teachua.ui.challenge

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.core.os.bundleOf
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import coil.compose.AsyncImage
import com.softserve.teachua.MainActivity
import com.softserve.teachua.R
import com.softserve.teachua.app.baseImageUrl
import com.softserve.teachua.app.tools.Resource
import com.softserve.teachua.data.model.ChallengeModel
import com.softserve.teachua.databinding.ChallengeFragmentBinding
import com.softserve.teachua.ui.compose.*
import com.softserve.teachua.ui.compose.theme.TeachUaComposeTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.challenge_fragment.view.*

@AndroidEntryPoint
class ChallengeFragment : Fragment() {

    private var _binding: ChallengeFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = ChallengeFragmentBinding.inflate(inflater, container, false)
        val view: View = binding.root
        (requireActivity() as MainActivity).toolbar.visibility = View.GONE
        var id = 1
        if (arguments != null){
            id = ChallengeFragmentArgs.fromBundle(requireArguments()).id
        }

        val compose = view.compose
        compose.setContent {
            TeachUaComposeTheme() {
                Challenge(id = id,
                    navigateToTask = { id ->
                        val bundle = bundleOf("id" to id)
                        Navigation.findNavController(view).navigate(R.id.action_challengeFragment_to_taskFragment, bundle)
                    },
                    onAppBarButtonClicked ={Navigation.findNavController(view).navigateUp()} )
            }
        }

        return view
    }
}

@Composable
fun Challenge(
    id: Int,
    navigateToTask: (Int) -> Unit,
    onAppBarButtonClicked: () -> Unit,
) {

    val challengeViewModel = hiltViewModel<ChallengeViewModel>()
    LaunchedEffect(key1 = true) {
        challengeViewModel.load(id)
    }
    val challenge by challengeViewModel.challenge.collectAsState(Resource.loading())

    Column(Modifier.background(MaterialTheme.colors.background)) {
        TopBar(
            title = if (challenge.data == null) "Підключення..." else challenge.data!!.title,
            buttonIcon = Icons.Filled.ArrowBack,
            onButtonClicked = onAppBarButtonClicked
        )
        ChallengePage(challenge, navigateToTask) { challengeViewModel.load(id) }
    }
}

@Composable
fun ChallengePage(
    challenge: Resource<ChallengeModel>,
    navigateToTask: (Int) -> Unit,
    onClick: () -> Unit) {
    ResourceWrapper(resource = challenge, onReloadButtonClick = { onClick() }) {

        val rememberScrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .padding(5.dp)
                .verticalScroll(rememberScrollState)
        ) {
            ImageAndTitleOnIt(challenge.data?.picture ?: "", challenge.data?.title ?: "")
            ShowLinks()
            HtmlTextWrapper(challenge.data?.description ?: "")
            BottomTasks(challenge, navigateToTask)
        }
    }

}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomTasks(challenge: Resource<ChallengeModel>, navigateToTask: (Int) -> Unit) {
    challenge.data?.let {
        LazyRow(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            items(it.tasks) { task ->
                Card(
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier
                        .height(215.dp)
                        .padding(10.dp)
                        .width(230.dp),
                    onClick = { navigateToTask(task.id) }
                ) {
                    Column(
                        modifier = Modifier
                            .padding(5.dp)
                    ) {
                        Surface(
                            color = MaterialTheme.colors.surface,
                            shape = MaterialTheme.shapes.medium,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp)
                        ) {
                            AsyncImage(
                                model = baseImageUrl + task.picture,
                                contentDescription = "",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }

                        val annotatedString = task.name
                        HtmlText(text = annotatedString,
                            modifier = Modifier.padding(4.dp),
                            maxLines = 3)
                    }

                }
            }
        }
    }
}


