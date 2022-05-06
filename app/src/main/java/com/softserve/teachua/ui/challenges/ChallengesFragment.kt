package com.softserve.teachua.ui.challenges

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.sharp.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.Navigation
import com.softserve.teachua.MainActivity
import com.softserve.teachua.R
import com.softserve.teachua.data.model.ChallengeModel
import com.softserve.teachua.databinding.FragmentChallengesBinding
import com.softserve.teachua.ui.challenge.ChallengeFragmentArgs
import com.softserve.teachua.ui.compose.ResourceWrapper
import com.softserve.teachua.ui.compose.TopBarWithSearchBtn
import com.softserve.teachua.ui.compose.theme.TeachUaComposeTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ChallengesFragment : Fragment() {

    private var _binding: FragmentChallengesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    private lateinit var compose: ComposeView


    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChallengesBinding.inflate(inflater, container, false)
        val view: View = binding.root
        (requireActivity() as MainActivity).toolbar.visibility = View.GONE


        compose = binding.compose
        compose.setContent {
            TeachUaComposeTheme() {
                Challenges(
                    title = "Challenges",
                    openDrawer = { (activity as MainActivity).openDrawer() },
                    onChallengeClick = { id ->
                        val bundle = bundleOf("id" to id)
                        Navigation.findNavController(view).navigate(R.id.challenges_to_challelnge, bundle)
                    },
                    goToSearchClick = {}
                )
            }
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


@OptIn(ExperimentalAnimationApi::class, ExperimentalComposeUiApi::class)
@Composable
fun Challenges(
    title: String,
    openDrawer: () -> Unit,
    onChallengeClick: (id: Int) -> Unit,
    goToSearchClick: () -> Unit
) {
    val viewModel = hiltViewModel<ChallengesViewModel>()
    LaunchedEffect(key1 = true) {
        viewModel.load()
    }
    val challenges by viewModel.challenges.collectAsState()


    Column(Modifier.background(MaterialTheme.colors.background)) {
        TopBarWithSearchBtn(
            title = title,
            buttonIcon = Icons.Filled.Menu,
            onNavBtnClicked = { openDrawer() },
            { goToSearchClick() }
        )


        ResourceWrapper(
            resource = challenges,
            onReloadButtonClick = {
                viewModel.load()

            },
        ) {
            challenges.data?.let { it1 -> ChallengesList(challenges = it1, onChallengeClick) }
        }
    }

}

@Composable
fun ChallengesList(challenges: List<ChallengeModel>, onChallengeClick: (id: Int) -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, bottom = 30.dp)
    ) {
        item { Spacer(modifier = Modifier.height(10.dp)) }
        items(challenges) {
            Row(modifier = Modifier
                .clickable { onChallengeClick(it.id) }
                .fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Sharp.Favorite, contentDescription = "")
                Spacer(Modifier.width(15.dp))
                Text(
                    text = it.name,
                    fontSize = 25.sp
                )

            }
            Spacer(Modifier.height(15.dp))

        }
    }
}