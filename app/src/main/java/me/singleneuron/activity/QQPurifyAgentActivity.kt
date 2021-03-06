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
package me.singleneuron.activity

import android.R
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import nil.nadph.qnotified.HookEntry
import nil.nadph.qnotified.lifecycle.JumpActivityEntryHook

class QQPurifyAgentActivity :AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var pkg: String? = null
        pkg = HookEntry.PACKAGE_NAME_QQ
        val intent = Intent()
        intent.component = ComponentName(pkg, "com.tencent.mobileqq.activity.JumpActivity")
        intent.action = Intent.ACTION_VIEW
        intent.putExtra(
            JumpActivityEntryHook.JUMP_ACTION_CMD,
            JumpActivityEntryHook.JUMP_ACTION_START_ACTIVITY)
        intent.putExtra(
            JumpActivityEntryHook.JUMP_ACTION_TARGET, "me.zpp0196.qqpurify.activity.MainActivity")
        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            AlertDialog.Builder(this).setTitle("出错啦")
                .setMessage("拉起模块设置失败, 请确认 $pkg 已安装并启用(没有被关冰箱或被冻结停用)\n$e")
                .setCancelable(true).setPositiveButton(R.string.ok, null).show()
        }
        //finish()
    }

    override fun onStart() {
        super.onStart()
        setVisible(true)
    }

}
