package com.daff.sesi2_hz.ir.daff.presentation.home.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.daff.sesi2_hz.ir.daff.data.model.BaseResponse
import com.daff.sesi2_hz.ir.daff.databinding.FragmentHomeBinding
import com.daff.sesi2_hz.ir.daff.di.DIModule
import com.daff.sesi2_hz.ir.daff.di.DaggerDIComponent
import com.daff.sesi2_hz.ir.daff.presentation.auth.view.LoginActivity
import com.daff.sesi2_hz.ir.daff.presentation.home.adapter.NewsAdapter
import com.daff.sesi2_hz.ir.daff.presentation.home.detail.view.DetailNewsActivity
import com.daff.sesi2_hz.ir.daff.presentation.home.viewmodel.HomeViewModel
import com.daff.sesi2_hz.ir.daff.utils.extension.showToast
import javax.inject.Inject

class HomeFragment : Fragment() {
	
	private var _binding: FragmentHomeBinding? = null
	private val binding get() = _binding!!

	@Inject
	lateinit var viewModelFactory: ViewModelProvider.Factory

	private lateinit var homeViewModel: HomeViewModel
	private lateinit var newsAdapter: NewsAdapter

	var page = 1
	var pageSize = 20
	var isPaginating = false
	var query = ""

	@SuppressLint("NotifyDataSetChanged")
	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {

		_binding = FragmentHomeBinding.inflate(inflater, container, false)

		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		DaggerDIComponent.builder()
			.dIModule(DIModule(requireContext()))
			.build().inject(this@HomeFragment)

		homeViewModel = ViewModelProvider(
			this,
			viewModelFactory
		)[HomeViewModel::class.java]

		newsAdapter = NewsAdapter(homeViewModel) { news ->
			val intent = Intent(requireContext(), DetailNewsActivity::class.java)
			intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
			intent.putExtra("news", news)
			startActivity(intent)
		}

		binding.listNews.apply {
			setHasFixedSize(true)
			layoutManager = LinearLayoutManager(context)
			adapter = newsAdapter
		}

//		Observe

		homeViewModel.isLastPage.observe(this.viewLifecycleOwner) {
			if (it) {
				binding.listNews.adapter?.notifyItemRemoved(
					binding.listNews.adapter?.itemCount ?: 0
				)
			}
		}

		homeViewModel.topHeadlines.observe(this.viewLifecycleOwner) { result ->
			when (result) {
				is BaseResponse.Loading -> {
					setLayoutLoadingState()
				}

				is BaseResponse.Success -> {
					result.data.let {
						newsAdapter?.setNewsList(it)
					}
					isPaginating = false
					setLayoutLoadedState()
				}

				is BaseResponse.Error -> {
					binding.errorTitle.text = result.code.toString()
					binding.errorMsg.text = result.message
					setLayoutErrorState()
				}
			}
		}

		homeViewModel.modeOffline.observe(this.viewLifecycleOwner, Observer {
			requireContext().showToast("Currently you are in offline mode, please check your internet connection")
		})

		homeViewModel.logoutState.observe(this.viewLifecycleOwner, Observer {
			if (it == "success") {
				val intent = Intent(requireContext(), LoginActivity::class.java).apply {
					flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
					addFlags(flags)
				}
				startActivity(intent)
			}
		})

		homeViewModel.getTopHeadlines(requireContext(), page, pageSize)

//		search article

		val searchView = binding.search
		searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
			override fun onQueryTextSubmit(query: String?): Boolean {
				searchView.clearFocus()
				if(query?.isNotBlank() === true){
					searchArticle(query)
				}
				return false
			}

			override fun onQueryTextChange(query: String?): Boolean {
				return false
			}
		})

		searchView.setOnCloseListener {
			getTopHeadlines()
			false
		}

	}

	private fun setLayoutLoadingState() {
		binding.loading.isVisible = true
		binding.listNews.isVisible = false
		binding.errorTitle.isVisible = false
		binding.errorMsg.isVisible = false
	}

	private fun setLayoutLoadedState() {
		binding.loading.isVisible = false
		binding.listNews.isVisible = true
		binding.errorTitle.isVisible = false
		binding.errorMsg.isVisible = false
	}

	private fun setLayoutErrorState() {
		binding.loading.isVisible = false
		binding.listNews.isVisible = false
		binding.errorTitle.isVisible = true
		binding.errorMsg.isVisible = true
	}

	fun getTopHeadlines() {
		query = ""
		page = 1
		homeViewModel.getTopHeadlines(requireContext(), page, pageSize)
	}

	fun searchArticle(query: String) {
		this.query = query
		page = 1
		homeViewModel.getEverything(page, pageSize, query)
	}
	
	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}