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
package com.rymmmmm.hook;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import nil.nadph.qnotified.hook.CommonDelayableHook;
import nil.nadph.qnotified.util.Initiator;
import nil.nadph.qnotified.util.LicenseStatus;
import nil.nadph.qnotified.util.Utils;

//屏蔽抖动窗口 作用暂时不明
public class DisableShakeWindow extends CommonDelayableHook {
    private final static DisableShakeWindow self = new DisableShakeWindow();

    public static DisableShakeWindow get() {
        return self;
    }

    protected DisableShakeWindow() {
        super("rq_disable_shake_window");
    }

    @Override
    public boolean initOnce() {
        try {
            for (int i = 1; i < 4; i++) {
                for (Method m : Initiator.load("com.tencent.mobileqq.activity.aio.helper.AIOShakeHelper$" + i).getDeclaredMethods()) {
                    if (m.getName().equals("run") && !Modifier.isStatic(m.getModifiers())) {
                        XposedBridge.hookMethod(m, new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                if (LicenseStatus.sDisableCommonHooks) return;
                                if (!isEnabled()) return;
                                param.setResult(null);
                            }
                        });
                    }
                }
            }
            return true;
        } catch (Throwable e) {
            Utils.log(e);
            return false;
        }
    }
}
