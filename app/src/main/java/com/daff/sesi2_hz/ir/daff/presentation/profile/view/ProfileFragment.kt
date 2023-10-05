package com.daff.sesi2_hz.ir.daff.presentation.profile.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.daff.sesi2_hz.ir.daff.databinding.FragmentProfileBinding
import com.daff.sesi2_hz.ir.daff.di.DIModule
import com.daff.sesi2_hz.ir.daff.di.DaggerDIComponent
import com.daff.sesi2_hz.ir.daff.presentation.auth.view.LoginActivity
import com.daff.sesi2_hz.ir.daff.presentation.profile.viewmodel.ProfileViewModel
import javax.inject.Inject

class ProfileFragment : Fragment() {
	
	private var _binding: FragmentProfileBinding? = null
	private val binding get() = _binding!!

	@Inject
	lateinit var viewModelFactory: ViewModelProvider.Factory

	private lateinit var profileViewModel: ProfileViewModel

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {

		_binding = FragmentProfileBinding.inflate(inflater, container, false)

		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		DaggerDIComponent.builder()
			.dIModule(DIModule(requireContext()))
			.build().inject(this@ProfileFragment)

		profileViewModel = ViewModelProvider(
			this,
			viewModelFactory
		)[ProfileViewModel::class.java]

		profileViewModel.getUserEmail()
		profileViewModel.getUsername()

		profileViewModel.userEmail.observe(this.viewLifecycleOwner, Observer {email ->
			if (email.isNotBlank()){
				binding.email.text = email
			}
		})

		profileViewModel.username.observe(this.viewLifecycleOwner, Observer {username ->
			if (username.isNotBlank()){
				binding.username.text = username
			}
		})

		binding.btnLogout.setOnClickListener {
			logout()
		}

		profileViewModel.logoutState.observe(this.viewLifecycleOwner, Observer {
			if (it == "success") {
				val intent = Intent(requireContext(), LoginActivity::class.java).apply {
					flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
					addFlags(flags)
				}
				startActivity(intent)
			}
		})
	}

	fun logout() {
		profileViewModel.logout()
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}