#!/usr/bin/env node

module.exports = function (context) {

    console.log('--------> hook application....');

    var fs = context.requireCordovaModule('fs'),
        path = context.requireCordovaModule('path');

    var platformRoot = path.join(context.opts.projectRoot, 'platforms/android/app/src/main');
    var manifestFile = path.join(platformRoot, 'AndroidManifest.xml');

    console.log('--------> manifestFile: ' + manifestFile);

    if (fs.existsSync(manifestFile)) {

        console.log('--------> manifestFile: reading....');

        fs.readFile(manifestFile, 'utf8', function (err, data) {
            if (err) {
                throw new Error('Unable to find AndroidManifest.xml: ' + err);
            }

            var appClass = 'com.nxt.MyApplication';

            if (data.indexOf(appClass) == -1) {

                var result = data.replace(/<application/g, '<application android:name="' + appClass + '"');

                fs.writeFile(manifestFile, result, 'utf8', function (err) {
                    if (err) throw new Error('Unable to write into AndroidManifest.xml: ' + err);
                })
            }
        });
    } else {
        console.log('--------> manifestFile: can not reading....');
    }
};