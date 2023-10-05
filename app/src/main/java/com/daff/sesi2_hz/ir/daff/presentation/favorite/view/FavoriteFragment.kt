package com.daff.sesi2_hz.ir.daff.presentation.favorite.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.daff.sesi2_hz.ir.daff.R
import com.daff.sesi2_hz.ir.daff.databinding.FragmentFavoriteBinding
import com.daff.sesi2_hz.ir.daff.di.DIModule
import com.daff.sesi2_hz.ir.daff.di.DaggerDIComponent
import com.daff.sesi2_hz.ir.daff.presentation.favorite.adapter.FavoriteAdapter
import com.daff.sesi2_hz.ir.daff.presentation.favorite.viewmodel.FavoriteViewModel
import com.daff.sesi2_hz.ir.daff.presentation.home.detail.view.DetailNewsActivity
import javax.inject.Inject

class FavoriteFragment : Fragment() {
	
	private var _binding: FragmentFavoriteBinding? = null
	private val binding get() = _binding!!

	@Inject
	lateinit var viewModelFactory: ViewModelProvider.Factory

	private lateinit var favoriteViewModel: FavoriteViewModel
	private lateinit var favoriteAdapter: FavoriteAdapter

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = FragmentFavoriteBinding.inflate(inflater, container, false)

		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		DaggerDIComponent.builder()
			.dIModule(DIModule(requireContext()))
			.build().inject(this@FavoriteFragment)

		favoriteViewModel = ViewModelProvider(
			this,
			viewModelFactory
		)[FavoriteViewModel::class.java]

		favoriteAdapter = FavoriteAdapter() { news ->
			val intent = Intent(requireContext(), DetailNewsActivity::class.java)
			intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
			intent.putExtra("news", news)
			startActivity(intent)
		}

		binding.listNews.apply {
			setHasFixedSize(true)
			layoutManager = LinearLayoutManager(context)
			adapter = favoriteAdapter
		}

		favoriteViewModel.getArticles()

		favoriteViewModel.articles.observe(this.viewLifecycleOwner, Observer { articles ->
			articles?.let {
				if (it.isNotEmpty()) {
					favoriteAdapter.setData(it)
					binding.errorTitle.isVisible = false
					binding.listNews.isVisible = true
				} else {
					binding.errorTitle.text = getString(R.string.empty_favorite)
					binding.errorTitle.isVisible = true
					binding.listNews.isVisible = false
				}
			}
		})
	}
	
	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}