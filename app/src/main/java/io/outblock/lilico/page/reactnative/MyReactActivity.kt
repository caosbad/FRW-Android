package io.outblock.lilico.page.reactnative

import android.os.Bundle
import com.facebook.react.BuildConfig
import com.facebook.react.PackageList
import com.facebook.react.ReactInstanceManager
import com.facebook.react.ReactRootView
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.common.LifecycleState
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler
import com.facebook.soloader.SoLoader
import com.microsoft.codepush.react.CodePush
import com.swmansion.reanimated.ReanimatedPackage
import io.outblock.lilico.base.activity.BaseActivity

class MyReactActivity : BaseActivity(), DefaultHardwareBackBtnHandler {
    private var mReactRootView: ReactRootView? = null
    private var mReactInstanceManager: ReactInstanceManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SoLoader.init(this, false)
        mReactRootView = ReactRootView(this)
        val packages = PackageList(application).getPackages()

        // Adding manually Reanimated package here, with overriding getReactInstanceManager method
        // Adding manually Reanimated package here, with overriding getReactInstanceManager method
        packages.add(object : ReanimatedPackage() {
            override fun getReactInstanceManager(reactContext: ReactApplicationContext): ReactInstanceManager {
                // Implement here your way to get the ReactInstanceManager
                return mReactInstanceManager!!
            }
        })
        mReactInstanceManager = ReactInstanceManager.builder()
            .setApplication(application)
            .setCurrentActivity(this)
            .setBundleAssetName("index.android.bundle")
            .setJSMainModulePath("index")
//            .setJSIModulesPackage(new ReanimatedJSIModulePackage()) // Adding ReanimatedJSIModulePackage here
            .addPackages(packages)
            .setUseDeveloperSupport(BuildConfig.DEBUG)
            .setInitialLifecycleState(LifecycleState.RESUMED)
            .setJSBundleFile(CodePush.getJSBundleFile())
            .build()

//        UpdateContext.setCustomInstanceManager(mReactInstanceManager)

        mReactRootView!!.startReactApplication(mReactInstanceManager, "frw-rn-native", null)
        setContentView(mReactRootView)
    }

    override fun invokeDefaultOnBackPressed() {
        super.onBackPressed()
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