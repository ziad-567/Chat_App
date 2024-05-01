package com.mis.route.chatapp.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.mis.route.chatapp.databinding.FragmentLoginBinding
import com.mis.route.chatapp.ui.showDialog


abstract class BaseFragment <VB:ViewBinding , VM:BaseViewModel >: Fragment() {

   lateinit var viewModel : VM

    private var _binding: VB? = null

    protected val binding: VB get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = initViewModel()
    }

    abstract fun initViewModel():VM

    abstract fun getLayoutId(): Int

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater,
         getLayoutId(),
         container
         ,false)
    return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.viewMessage.observe(viewLifecycleOwner){
        showDialog(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }

    //abstract fun observeLiveData()
}