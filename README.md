<!-- # Title -->
# Marvel
![Demo](https://upload.wikimedia.org/wikipedia/commons/thumb/b/b9/Marvel_Logo.svg/1200px-Marvel_Logo.svg.png)


<!-- # Short Description -->

>- Using this application, the user is able to reach all the **characters** of Marvel Universe and see all the **comics** around it
>- *Search* and *favorite* the **characters** you want to easily reach it

This application was created to *study*, *improve* and *show* the best techniques and approaches that is used in high quality applications, such as
architectures (in this specific app, using **MVVM**), **Coroutines**, **Hilt**, **Room** and much more. 


<!-- # Badges -->
<div style="display: inline_block"><br>
    <img height="30" width="40" src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/androidstudio/androidstudio-original.svg">
    <img height="30" width="40" src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/kotlin/kotlin-original.svg">
</div>

---

# Tags

`Android Studio` `Kotlin` `Room` `OkHTTP` `Coroutines` `MVVM` `Retrofit` `Hilt`

---


# Demo

![Demo](https://media.discordapp.net/attachments/655489748885831713/1044744956620832778/gif1.gif)


>- Open your app with a clean animation


![Demo](https://media.discordapp.net/attachments/655489748885831713/1044744957585522708/gif_2.gif)


>- Search through all the **Marvel Universe** to reach all the characters
>- Click in the description to see the full information provided by **Marvel** for the selected *character*
>- See all the *comics* related to the selected *character*
  
![Demo](https://media.discordapp.net/attachments/655489748885831713/1044744957233209435/gif3.gif)


>- **Search** through all the **Marvel Universe** to easily reach the **character** and **comics** that you want

![Demo](https://media.discordapp.net/attachments/655489748885831713/1044744956931223582/gif4.gif)


>- Create your own *favorite* list and easily find your most loved **characters**
>- *Add* and *delete* **characters** to your list any time you want

---

# Code Example
```kotlin
abstract class BaseFragment<VB: ViewBinding, VM: ViewModel>: Fragment() {
    private var _binding: VB? = null
    protected val binding get() = _binding!!
    protected abstract val viewModel: VM

    override fun onCreateView
                (inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = recoverViewBinding(inflater, container)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    protected abstract fun recoverViewBinding(inflater: LayoutInflater, container: ViewGroup?): VB
}
```

Abstract class created to avoid using the same code many times for different ocasions.
This class is responsible to provide for the fragment that extends from *BaseFragment*, all the needed settup needed to use **ViewBinding** and **ViewModel**.

---

# Libraries

>- [Timber](https://github.com/JakeWharton/timber)
>- [ROOM](https://developer.android.com/jetpack/androidx/releases/room?hl=pt-br)
>- [Glide](https://github.com/bumptech/glide)
>- [Lifecycle](https://developer.android.com/jetpack/androidx/releases/lifecycle)
>- [Coroutines](https://developer.android.com/kotlin/coroutines?hl=pt-br)
>- [KTX](https://developer.android.com/kotlin/ktx)
>- [Retrofit](https://square.github.io/retrofit/)
>- [OkHTTP](https://square.github.io/okhttp/)
>- [Navigation Components](https://developer.android.com/guide/navigation)
>- [Hilt](https://dagger.dev/hilt/)

---

# Contributors

- [Thiago Rodrigues](https://www.linkedin.com/in/tods/)
