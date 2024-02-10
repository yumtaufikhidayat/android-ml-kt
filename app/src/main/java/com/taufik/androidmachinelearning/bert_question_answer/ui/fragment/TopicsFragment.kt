package com.taufik.androidmachinelearning.bert_question_answer.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.taufik.androidmachinelearning.R
import com.taufik.androidmachinelearning.bert_question_answer.DataSetClient
import com.taufik.androidmachinelearning.bert_question_answer.ui.adapter.TopicsAdapter
import com.taufik.androidmachinelearning.databinding.FragmentTopicsBinding


class TopicsFragment : Fragment() {

    private var _binding: FragmentTopicsBinding? = null
    private val binding get() = _binding!!

    private var topicsAdapter: TopicsAdapter? = null
    private var topicsTitle = emptyList<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTopicsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val linearLayoutManager = LinearLayoutManager(requireContext())
        val decoration = DividerItemDecoration(
            binding.rvTopics.context,
            linearLayoutManager.orientation
        )

        val dataSetClient = DataSetClient(requireActivity())
        dataSetClient.loadJsonData()?.let {
            topicsTitle = it.getTitles()
        }

        topicsAdapter = TopicsAdapter(topicsTitle, object: TopicsAdapter.OnItemSelected{
            override fun onItemClicked(itemID: Int, itemTitle: String) {
                startQaScreen(itemID, itemTitle)
            }
        })

        with(binding.rvTopics) {
            layoutManager = linearLayoutManager
            adapter = topicsAdapter
            addItemDecoration(decoration)
        }
    }

    private fun startQaScreen(itemID: Int, itemTitle: String) {
        val action = TopicsFragmentDirections.actionTopicsFragmentToQAFragment(
            itemID,
            itemTitle
        )
        Navigation.findNavController(requireActivity(), R.id.fragment_container).navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}