/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.guesstheword.screens.game

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.example.android.guesstheword.R
import com.example.android.guesstheword.databinding.GameFragmentBinding

/**
 * Fragment where the game is played
 */
class GameFragment : Fragment() {

    private lateinit var binding: GameFragmentBinding

    //Creating the viewModel for GameFragment
    private lateinit var viewModel: GameViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate view and obtain an instance of the binding class
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.game_fragment,
                container,
                false
        )



        Log.i("GameFragment", "View model variable initiated")
        viewModel = ViewModelProvider(this).get(GameViewModel::class.java)

        binding.setLifecycleOwner(this)
        //Removed observer since used data binding with Live Data directly on layout
         //  viewModel.score.observe(viewLifecycleOwner, Observer { newScore ->
         //    binding.scoreText.text = newScore.toString()
         // })

        //viewModel.word.observe(viewLifecycleOwner, Observer { newWord ->
        //    binding.wordText.text = newWord ?: ""
        // })

        viewModel.eventGameFinish.observe(viewLifecycleOwner, Observer { hasFinished ->
            if(hasFinished) {
                gameFinished()
                viewModel.onGameFinishComplete()
            }
        })

        //Removed this observer since added this login to ViewModel and then directly to layout
        //  viewModel.currentTime.observe(viewLifecycleOwner, Observer { nextTime ->
        //    Log.i("Time", nextTime.toString())
        //  binding.timerText.text = DateUtils.formatElapsedTime(nextTime)
        // })

        binding.gameViewModel = viewModel


        //Added onClick methods directly in xml through data binding

        //binding.correctButton.setOnClickListener {
        //  viewModel.onCorrect()
        //}
        //  binding.skipButton.setOnClickListener {
        //    viewModel.onSkip()
       //}

        return binding.root

    }


    /**
     * Called when the game is finished
     */
    private fun gameFinished() {
        val action = GameFragmentDirections.actionGameToScore(viewModel.score.value ?: 0)
        // val currentScore = viewModel.score.value ?: 0
        // action.setScore(currentScore) -> in case we dont add params in the function above
        findNavController(this).navigate(action)
    }

    private fun buzz(pattern: LongArray) {
        val buzzer= activity!!.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        //Vibrator v = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);

        buzzer?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                buzzer.vibrate(VibrationEffect.createWaveform(pattern, -1))
            } else {
                //deprecated in API 26
                buzzer.vibrate(pattern, -1)
            }
        }
    }

}
