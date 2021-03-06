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
package nil.nadph.qnotified.hook;

import android.os.Looper;
import android.widget.Toast;

import java.lang.reflect.Method;
import java.util.List;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import nil.nadph.qnotified.SyncUtils;
import nil.nadph.qnotified.config.ConfigManager;
import nil.nadph.qnotified.step.DexDeobfStep;
import nil.nadph.qnotified.step.Step;
import nil.nadph.qnotified.util.DexKit;
import nil.nadph.qnotified.util.Utils;

import static nil.nadph.qnotified.util.Initiator._EmoAddedAuthCallback;
import static nil.nadph.qnotified.util.Initiator._FavEmoRoamingHandler;
import static nil.nadph.qnotified.util.Utils.*;

public class FavMoreEmo extends CommonDelayableHook {
    private static final FavMoreEmo self = new FavMoreEmo();

    FavMoreEmo() {
        super("qqhelper_fav_more_emo", new DexDeobfStep(DexKit.C_FAV_EMO_CONST));
    }

    public static FavMoreEmo get() {
        return self;
    }

    @Override
    public boolean initOnce() {
        try {
            final Class mEmoAddedAuthCallback = _EmoAddedAuthCallback();
            final Class mFavEmoRoamingHandler = _FavEmoRoamingHandler();
            if (mEmoAddedAuthCallback == null) {
                if (mFavEmoRoamingHandler == null) {
                    setEmoNum();
                } else {
                    XposedHelpers.findAndHookMethod(mFavEmoRoamingHandler, "a", List.class, List.class, new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            setEmoNum();
                        }
                    });
                }
            } else {
                Class mUpCallBack$SendResult = null;
                for (Method m : mEmoAddedAuthCallback.getDeclaredMethods()) {
                    if (m.getName().equals("b") && m.getReturnType().equals(void.class) && m.getParameterTypes().length == 1) {
                        mUpCallBack$SendResult = m.getParameterTypes()[0];
                        break;
                    }
                }
                XposedHelpers.findAndHookMethod(mEmoAddedAuthCallback, "b", mUpCallBack$SendResult, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        Object msg = param.args[0];
                        iput_object(msg, "a", int.class, 0);
                    }
                });
                XposedHelpers.findAndHookMethod(mFavEmoRoamingHandler, "a", List.class, List.class, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        setEmoNum();
                    }
                });
            }
            return true;
        } catch (Throwable e) {
            log(e);
            return false;
        }
    }

    private void setEmoNum() {
        Class mFavEmoConstant = DexKit.doFindClass(DexKit.C_FAV_EMO_CONST);
        sput_object(mFavEmoConstant, "a", 800);
        sput_object(mFavEmoConstant, "b", 800);
    }
}
