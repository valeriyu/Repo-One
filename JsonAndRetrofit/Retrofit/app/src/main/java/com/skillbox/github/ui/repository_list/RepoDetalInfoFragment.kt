package com.skillbox.github.ui.repository_list

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.navArgs
import androidx.viewbinding.ViewBinding
import com.skillbox.github.R
import com.skillbox.github.databinding.FragmentRepoDetalInfoBinding
import kotlinx.android.synthetic.main.fragment_repo_detal_info.*
import kotlinx.android.synthetic.main.fragment_repository_list.*


class RepoDetalInfoFragment : Fragment(R.layout.fragment_repo_detal_info) {

    //private val viewModel: RepositoriesViewModel by activityViewModels()
    private val viewModel: RepositoriesViewModel by viewModels()
    private var isStarred = false
    private  var binding:FragmentRepoDetalInfoBinding? = null


    private val args: RepoDetalInfoFragmentArgs by navArgs()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding = FragmentRepoDetalInfoBinding.inflate(layoutInflater)

        chStarredBtn.setOnClickListener {
            viewModel.setStar(args.owner.toString(), args.repo.toString(), !isStarred)
        }

        viewModel.isStarred.observe(viewLifecycleOwner) {
            isStarred = it
            if (isStarred) {
                chStarredBtn.text = "СНЯТЬ"
                chStarredBtn.setCompoundDrawablesWithIntrinsicBounds(
                    ContextCompat.getDrawable(requireContext(),R.drawable.ic_baseline_star_rate_24),    // left
                    null,
                    null,
                    null
                )
            } else {
                chStarredBtn.text = "ПОСТАВИТЬ"
                chStarredBtn.setCompoundDrawablesWithIntrinsicBounds(
                    ContextCompat.getDrawable(requireContext(),R.drawable.ic_baseline_star_outline_24),    // left
                    null,
                    null,
                    null
                )
            }
        }

        viewModel.repo.observe(viewLifecycleOwner) {repo ->
            if(repo == null) return@observe

            repoTextViw.text = "id: ${repo.id}\n Название: ${repo.name}\n Владелец: ${repo.owner.username}\n"
        }

        viewModel.isLoading.observe(viewLifecycleOwner) {
            if (it) {
                chStarredBtn.text = ""
                binding!!.progressBar.visibility = View.VISIBLE
            }else{
                binding!!.progressBar.visibility = View.GONE
            }
        }

        viewModel.getRepo(args.owner.toString(), args.repo.toString())
        viewModel.checkRepositoryIsStarred(args.owner.toString(), args.repo.toString())

        viewModel.error.observe(viewLifecycleOwner){
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}