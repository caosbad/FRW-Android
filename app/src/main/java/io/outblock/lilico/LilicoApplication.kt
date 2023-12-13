package io.outblock.lilico

import android.app.Application
import com.facebook.react.PackageList
import com.facebook.react.ReactApplication
import com.facebook.react.ReactNativeHost
import com.facebook.react.ReactPackage
import com.facebook.react.defaults.DefaultReactNativeHost
import io.outblock.lilico.manager.LaunchManager
import io.outblock.lilico.utils.Env
import expo.modules.ReactNativeHostWrapper;


class LilicoApplication : Application(), ReactApplication  {

    override fun onCreate() {
        super.onCreate()
        Env.init(this)
        LaunchManager.init(this)
    }

    //* rn **//
    private val mReactNativeHost: ReactNativeHost? =
        ReactNativeHostWrapper(this, object : DefaultReactNativeHost(this) {
            override fun getUseDeveloperSupport(): Boolean {
                return BuildConfig.DEBUG
            }

            override fun getPackages(): List<ReactPackage> {
                // Packages that cannot be autolinked yet can be added manually here, for example:
                // packages.add(new MyReactNativePackage());
                return PackageList(this).packages
            }

            override fun getJSMainModuleName(): String {
                return ".expo/.virtual-metro-entry"
            }

            override val isNewArchEnabled: Boolean
                protected get() = BuildConfig.IS_NEW_ARCHITECTURE_ENABLED
            override val isHermesEnabled: Boolean?
                protected get() = BuildConfig.IS_HERMES_ENABLED
        })
    override fun getReactNativeHost(): ReactNativeHost? {
        return mReactNativeHost
    }

}