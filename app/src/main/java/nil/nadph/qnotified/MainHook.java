/* QNotified - An Xposed module for QQ/TIM
 * Copyright (C) 2019-2021 xenonhydride@gmail.com
 * https://github.com/ferredoxin/QNotified
 *
 * This software is free software: you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this software.  If not, see
 * <https://www.gnu.org/licenses/>.
 */
package nil.nadph.qnotified;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.*;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.lang.reflect.*;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import me.kyuubiran.hook.RemoveCameraButton;
import me.kyuubiran.hook.RemoveRedDot;
import nil.nadph.qnotified.config.ConfigItems;
import nil.nadph.qnotified.hook.*;
import com.rymmmmm.hook.CustomSplash;
import nil.nadph.qnotified.lifecycle.ActProxyMgr;
import nil.nadph.qnotified.lifecycle.JumpActivityEntryHook;
import nil.nadph.qnotified.lifecycle.Parasitics;
import nil.nadph.qnotified.ui.ResUtils;
import nil.nadph.qnotified.util.*;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import static nil.nadph.qnotified.util.Initiator._StartupDirector;
import static nil.nadph.qnotified.util.Initiator.load;
import static nil.nadph.qnotified.util.Utils.*;

/*TitleKit:Lcom/tencent/mobileqq/widget/navbar/NavBarCommon*/

@SuppressWarnings("rawtypes")
public class MainHook {

    private static MainHook SELF;

    boolean third_stage_inited = false;

    private MainHook() {
    }

    public static MainHook getInstance() {
        if (SELF == null) {
            SELF = new MainHook();
        }
        return SELF;
    }

    public static XC_MethodHook.Unhook findAndHookMethodIfExists(Class<?> clazz, String methodName, Object...
        parameterTypesAndCallback) {
        try {
            return findAndHookMethod(clazz, methodName, parameterTypesAndCallback);
        } catch (Throwable e) {
            log(e);
            return null;
        }
    }

    public static XC_MethodHook.Unhook findAndHookMethodIfExists(String clazzName, ClassLoader cl, String
        methodName, Object... parameterTypesAndCallback) {
        try {
            return findAndHookMethod(clazzName, cl, methodName, parameterTypesAndCallback);
        } catch (Throwable e) {
            log(e);
            return null;
        }
    }

    /**
     * @deprecated Use {@link Activity#startActivity(Intent)} directly instead.
     */
    public static void startProxyActivity(Context ctx, int action) {
        ctx.startActivity(new Intent(ctx, ActProxyMgr.getActivityByAction(action)));
    }

    /**
     * @deprecated Use {@link Activity#startActivity(Intent)} directly instead.
     */
    public static void startProxyActivity(Context ctx, Class<?> clz) {
        ctx.startActivity(new Intent(ctx, clz));
    }

    public static void openProfileCard(Context ctx, long uin) {
        try {
            Parcelable allInOne = (Parcelable) new_instance(load("com/tencent/mobileqq/activity/ProfileActivity$AllInOne"), "" + uin, 35, String.class, int.class);
            Intent intent = new Intent(ctx, load("com/tencent/mobileqq/activity/FriendProfileCardActivity"));
            intent.putExtra("AllInOne", allInOne);
            ctx.startActivity(intent);
        } catch (Exception e) {
            log(e);
        }
    }

    /**
     * A屏黑主题,自用
     */
    public static void deepDarkTheme() {
        if (!SyncUtils.isMainProcess()) return;
        if (getLongAccountUin() != 1041703712) return;
        try {
            Class clz = load("com/tencent/mobileqq/activity/FriendProfileCardActivity");
            findAndHookMethod(clz, "doOnCreate", Bundle.class, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    final Activity ctx = (Activity) param.thisObject;
                    FrameLayout frame = ctx.findViewById(android.R.id.content);
                    frame.getChildAt(0).setBackgroundColor(0xFF000000);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException ignored) {
                            }
                            ctx.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        View frame = ctx.findViewById(android.R.id.content);
                                        frame.setBackgroundColor(0xFF000000);
                                        View dk0 = ctx.findViewById(ctx.getResources().getIdentifier("dk0", "id", ctx.getPackageName()));
                                        if (dk0 != null) dk0.setBackgroundColor(0x00000000);
                                    } catch (Exception e) {
                                        log(e);
                                    }
                                }
                            });
                        }
                    }).start();
                }
            });
            clz = load("com.tencent.mobileqq.activity.ChatSettingForTroop");
            findAndHookMethod(clz, "doOnCreate", Bundle.class, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    final Activity ctx = (Activity) param.thisObject;
                    FrameLayout frame = ctx.findViewById(android.R.id.content);
                    frame.getChildAt(0).setBackgroundColor(0xFF000000);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException ignored) {
                            }
                            ctx.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        FrameLayout frame = ctx.findViewById(android.R.id.content);
                                        frame.getChildAt(0).setBackgroundColor(0xFF000000);
                                        ViewGroup list = ctx.findViewById(ctx.getResources().getIdentifier("common_xlistview", "id", ctx.getPackageName()));
                                        list.getChildAt(0).setBackgroundColor(0x00000000);
                                    } catch (Exception e) {
                                        log(e);
                                    }
                                }
                            });
                        }
                    }).start();
                }
            });
            clz = load("com.tencent.mobileqq.activity.TroopMemberListActivity");
            findAndHookMethod(clz, "doOnCreate", Bundle.class, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    final Activity ctx = (Activity) param.thisObject;
                    FrameLayout frame = ctx.findViewById(android.R.id.content);
                    frame.getChildAt(0)/*.getChildAt(0)*/.setBackgroundColor(0xFF000000);
                }
            });
        } catch (Exception e) {
            log(e);
        }
    }

    public void performHook(Context ctx, Object step) {
        //ConfigTable.INSTANCE.setTim(isTim(ctx));
        SyncUtils.initBroadcast(ctx);
//        if (SyncUtils.getProcessType() == SyncUtils.PROC_MSF) {
//            Debug.waitForDebugger();
//        }
        try {
            Class<?> _NewRuntime = Initiator.load("com.tencent.mobileqq.startup.step.NewRuntime");
            Method[] methods = _NewRuntime.getDeclaredMethods();
            Method doStep = null;
            if (methods.length == 1) {
                doStep = methods[0];
            } else {
                for (Method m : methods) {
                    if (Modifier.isProtected(m.getModifiers()) || m.getName().equals("doStep")) {
                        doStep = m;
                        break;
                    }
                }
            }
            XposedBridge.hookMethod(doStep, new XC_MethodHook(52) {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    Utils.$access$set$sAppRuntimeInit(true);
                    //logi("NewRuntime/I after doStep");
                    if (SyncUtils.isMainProcess()) {
                        // fix error in :video, and QZone启动失败
                        LicenseStatus.sDisableCommonHooks = LicenseStatus.isLoadingDisabled() || LicenseStatus.isBlacklisted() || LicenseStatus.isSilentGone();
                    }
                }
            });
        } catch (Throwable e) {
            loge("NewRuntime/E hook failed: " + e);
            Utils.$access$set$sAppRuntimeInit(true);
        }
        injectLifecycleForProcess(ctx);
        BaseDelayableHook.allowEarlyInit(RevokeMsgHook.get());
        BaseDelayableHook.allowEarlyInit(MuteQZoneThumbsUp.get());
        BaseDelayableHook.allowEarlyInit(MuteAtAllAndRedPacket.get());
        BaseDelayableHook.allowEarlyInit(GagInfoDisclosure.get());
        BaseDelayableHook.allowEarlyInit(CustomSplash.get());
        BaseDelayableHook.allowEarlyInit(RemoveCameraButton.get());
        BaseDelayableHook.allowEarlyInit(RemoveRedDot.INSTANCE);
        if (SyncUtils.isMainProcess()) {
            ConfigItems.removePreviousCacheIfNecessary();
            injectStartupHookForMain(ctx);
            Class loadData = load("com/tencent/mobileqq/startup/step/LoadData");
            Method doStep = null;
            for (Method method : loadData.getDeclaredMethods()) {
                if (method.getReturnType().equals(boolean.class) && method.getParameterTypes().length == 0) {
                    doStep = method;
                    break;
                }
            }
            XposedBridge.hookMethod(doStep, new XC_MethodHook(51) {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    if (third_stage_inited) return;
                    Class director = _StartupDirector();
                    Object dir = iget_object_or_null(param.thisObject, "mDirector", director);
                    if (dir == null) dir = iget_object_or_null(param.thisObject, "a", director);
                    if (dir == null) dir = getFirstNSFByType(param.thisObject, director);
                    if (SyncUtils.isMainProcess()) {
                        ResUtils.loadThemeByArsc(getApplication(), false);
                    }
                    InjectDelayableHooks.step(dir);
                    onAppStartupForMain();
                    third_stage_inited = true;
                    //startFakeString();
                }
            });
        } else {
            if (LicenseStatus.hasUserAcceptEula()) {
                Class director = _StartupDirector();
                Object dir = iget_object_or_null(step, "mDirector", director);
                if (dir == null) dir = iget_object_or_null(step, "a", director);
                if (dir == null) dir = getFirstNSFByType(step, director);
                InjectDelayableHooks.step(dir);
            }
        }
    }

    private static void injectLifecycleForProcess(Context ctx) {
        if (SyncUtils.isMainProcess()) {
            Parasitics.injectModuleResources(ctx.getApplicationContext().getResources());
        }
        if (SyncUtils.isTargetProcess(SyncUtils.PROC_MAIN | SyncUtils.PROC_PEAK)) {
            Parasitics.initForStubActivity(ctx);
        }
    }

    @MainProcess
    private void injectStartupHookForMain(Context ctx) {
        JumpActivityEntryHook.initForJumpActivityEntry(ctx);
    }

    /**
     * dummy method, for development and test only
     */
    public static void onAppStartupForMain() {
        if (!isAlphaVersion()) return;
        deepDarkTheme();
    }

}
