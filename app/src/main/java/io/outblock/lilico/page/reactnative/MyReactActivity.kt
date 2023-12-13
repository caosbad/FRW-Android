package io.outblock.lilico.page.reactnative

//import io.outblock.lilico.base.activity.BaseActivity

import android.os.Build
import android.os.Bundle
import com.facebook.react.BuildConfig
import com.facebook.react.PackageList
import com.facebook.react.ReactActivity
import com.facebook.react.ReactActivityDelegate
import com.facebook.react.ReactInstanceManager
import com.facebook.react.ReactRootView
import com.facebook.react.common.LifecycleState
import com.facebook.react.defaults.DefaultNewArchitectureEntryPoint.fabricEnabled
import com.facebook.react.defaults.DefaultReactActivityDelegate
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler
import com.facebook.soloader.SoLoader
import expo.modules.ReactActivityDelegateWrapper


class MyReactActivity : ReactActivity(), DefaultHardwareBackBtnHandler {
    private var mReactRootView: ReactRootView? = null
    private var mReactInstanceManager: ReactInstanceManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SoLoader.init(this, false)
        mReactRootView = ReactRootView(this)
        val packages = PackageList(application).getPackages()
        // 有一些第三方可能不能自动链接，对于这些包我们可以用下面的方式手动添加进来：
        // packages.add(new MyReactNativePackage());
        // 同时需要手动把他们添加到settings.gradle和 app/build.gradle配置文件中。
        mReactInstanceManager = ReactInstanceManager.builder()
            .setApplication(application)
            .setCurrentActivity(this)
            .setBundleAssetName("index.android.bundle")
            .setJSMainModulePath("index")
            .addPackages(packages)
            .setUseDeveloperSupport(BuildConfig.DEBUG)
            .setInitialLifecycleState(LifecycleState.RESUMED)
            .build()
        // 注意这里的MyReactNativeApp 必须对应"index.js"中的
        // "AppRegistry.registerComponent()"的第一个参数
        mReactRootView!!.startReactApplication(mReactInstanceManager, "MyReactNativeApp", null)
        setContentView(mReactRootView)
    }

//    override fun invokeDefaultOnBackPressed() {
//        super.onBackPressed()
//    }

    override fun invokeDefaultOnBackPressed() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.R) {
            if (!moveTaskToBack(false)) {
                // For non-root activities, use the default implementation to finish them.
                super.invokeDefaultOnBackPressed()
            }
            return
        }

        // Use the default back button implementation on Android S
        // because it's doing more than {@link Activity#moveTaskToBack} in fact.
        super.invokeDefaultOnBackPressed()
    }

    override fun getMainComponentName(): String? {
        return "main"
    }

    override fun createReactActivityDelegate(): ReactActivityDelegate? {
        return ReactActivityDelegateWrapper(
            this, BuildConfig.IS_NEW_ARCHITECTURE_ENABLED, DefaultReactActivityDelegate(
                this,
                mainComponentName!!,  // If you opted-in for the New Architecture, we enable the Fabric Renderer.
                fabricEnabled
            )
        )
    }


    override fun onPause() {
        super.onPause()
        if (mReactInstanceManager != null) {
            mReactInstanceManager!!.onHostPause(this)
        }
    }

    override fun onResume() {
        super.onResume()
        if (mReactInstanceManager != null) {
            mReactInstanceManager!!.onHostResume(this, this)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mReactInstanceManager != null) {
            mReactInstanceManager!!.onHostDestroy(this)
        }
        if (mReactRootView != null) {
            mReactRootView!!.unmountReactApplication()
        }
    }

    override fun onBackPressed() {
        if (mReactInstanceManager != null) {
            mReactInstanceManager!!.onBackPressed()
        } else {
            super.onBackPressed()
        }
    }
}