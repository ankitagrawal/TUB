/*
 * this file contains the following plugins
 * slowFade, stepper, labelify, form, jqModal (along with jq drag'n'resize), getErrorHtmlFromJsonResponse
 */
// extend jquery for slowFade effect. not making separate plugin as this is used everywhere
jQuery.fn.slowFade = function(showTime, fadeTime, callback) {
  return this.each(function() {
    if (!showTime) showTime=1000;
    if (!fadeTime) fadeTime=1000;
    $(this).show().animate({opacity: 1.0}, showTime).fadeOut(fadeTime, callback);
  });
};
/*
 * jqStepper plugin. Not very well written as I do not know how to write jquery plugins.
 * uses global variables for max, min, onChange values
 * Probably should NOT be used ANYWHERE ELSE!
 */
jQuery.fn.jqStepper = function(minValue, maxValue) {
  jQuery.fn.jqStepper.minValue = minValue;
  jQuery.fn.jqStepper.maxValue = maxValue;
  return this.each(jqStepper_addStepper);
  function jqStepper_addStepper() {
    $(this).wrap('<span class="jqStepper"/>').after('<a href="#" class="jqStepper-up"><span>&gt;</span></a><a href="#" class="jqStepper-down"><span>&lt;</span></a>');
    $(this).parents('.jqStepper').find('.jqStepper-up').click(jqStepper_increaseValue);
    $(this).parents('.jqStepper').find('.jqStepper-down').click(jqStepper_decreaseValue);
  }
  function jqStepper_increaseValue() {
    var inputElem = $(this).parents('.jqStepper').find('input');
    var val = eval(inputElem.val()+'+'+1);
    if(!jQuery.fn.jqStepper.maxValue) inputElem.val(val);
    else if (val <= jQuery.fn.jqStepper.maxValue) inputElem.val(val);
    inputElem.blur();
    return false;
  }
  function jqStepper_decreaseValue() {
    var inputElem = $(this).parents('.jqStepper').find('input');
    var val = eval(inputElem.val()+'-'+1);
    if(!jQuery.fn.jqStepper.minValue) inputElem.val(val);
    else if (val >= jQuery.fn.jqStepper.minValue) inputElem.val(val);
    inputElem.blur();
    return false;
  }
};
/**
 * jQuery.labelify - Display in-textbox hints
 * Stuart Langridge, http://www.kryogenix.org/
 * Released into the public domain
 * Date: 25th June 2008
 * @author Stuart Langridge
 * @version 1.3
 *
 *
 * Basic calling syntax: $("input").labelify();
 * Defaults to taking the in-field label from the field's title attribute
 *
 * You can also pass an options object with the following keys:
 *   text
 *     "title" to get the in-field label from the field's title attribute
 *      (this is the default)
 *     "label" to get the in-field label from the inner text of the field's label
 *      (note that the label must be attached to the field with for="fieldid")
 *     a function which takes one parameter, the input field, and returns
 *      whatever text it likes
 *
 *   labelledClass
 *     a class that will be applied to the input field when it contains the
 *      label and removed when it contains user input. Defaults to blank.
 *
 */
jQuery.fn.labelify = function(settings) {
  settings = jQuery.extend({
    text: "title",
    labelledClass: "",
    defaultValueBlank: true
  }, settings);
  var lookups = {
    title: function(input) {
      return $(input).attr("title");
    },
    label: function(input) {
      return $("label[for=" + input.id +"]").text();
    }
  };
  var lookup;
  var jQuery_labellified_elements = $(this);
  return $(this).each(function() {
    var defaultValue = settings.defaultValueBlank?'':this.defaultValue;
    if (typeof settings.text === "string") {
      lookup = lookups[settings.text]; // what if not there?
    } else {
      lookup = settings.text; // what if not a fn?
    };
    // bail if lookup isn't a function or if it returns undefined
    if (typeof lookup !== "function") { return; }
    var lookupval = lookup(this);
    if (!lookupval) { return; }

    // need to strip newlines because the browser strips them
    // if you set textbox.value to a string containing them
    $(this).data("label",lookup(this).replace(/\n/g,''));
    $(this).focus(function() {
      if (this.value === $(this).data("label")) {
        this.value = defaultValue;
        $(this).removeClass(settings.labelledClass);
      }
    }).blur(function(){
      if (this.value === defaultValue) {
        this.value = $(this).data("label");
        $(this).addClass(settings.labelledClass);
      }
    });

    var removeValuesOnExit = function() {
      jQuery_labellified_elements.each(function(){
        if (this.value === $(this).data("label")) {
          this.value = defaultValue;
          $(this).removeClass(settings.labelledClass);
        }
      });
    };

    $(this).parents("form").submit(removeValuesOnExit);
    $(window).unload(removeValuesOnExit);

    if (this.value !== defaultValue) {
      // user already started typing; don't overwrite their work!
      return;
    }
    // actually set the value
    this.value = $(this).data("label");
    $(this).addClass(settings.labelledClass);

  });
};
/*
 * jQuery Form Plugin
 * version: 2.17 (06-NOV-2008)
 * @requires jQuery v1.2.2 or later
 *
 * Examples and documentation at: http://malsup.com/jquery/form/
 * Dual licensed under the MIT and GPL licenses:
 *   http://www.opensource.org/licenses/mit-license.php
 *   http://www.gnu.org/licenses/gpl.html
 *
 * Revision: $Id: jquery.form.js,v 1.2 2008-12-29 06:59:12 Kani Exp $
 */
;(function($) {

/*
    Usage Note:
    -----------
    Do not use both ajaxSubmit and ajaxForm on the same form.  These
    functions are intended to be exclusive.  Use ajaxSubmit if you want
    to bind your own submit handler to the form.  For example,

    $(document).ready(function() {
        $('#myForm').bind('submit', function() {
            $(this).ajaxSubmit({
                target: '#output'
            });
            return false; // <-- important!
        });
    });

    Use ajaxForm when you want the plugin to manage all the event binding
    for you.  For example,

    $(document).ready(function() {
        $('#myForm').ajaxForm({
            target: '#output'
        });
    });

    When using ajaxForm, the ajaxSubmit function will be invoked for you
    at the appropriate time.
*/

/**
 * ajaxSubmit() provides a mechanism for immediately submitting
 * an HTML form using AJAX.
 */
$.fn.ajaxSubmit = function(options) {
    // fast fail if nothing selected (http://dev.jquery.com/ticket/2752)
    if (!this.length) {
        log('ajaxSubmit: skipping submit process - no element selected');
        return this;
    }

    if (typeof options == 'function')
        options = { success: options };

    options = $.extend({
        url:  this.attr('action') || window.location.toString(),
        type: this.attr('method') || 'GET'
    }, options || {});

    // hook for manipulating the form data before it is extracted;
    // convenient for use with rich editors like tinyMCE or FCKEditor
    var veto = {};
    this.trigger('form-pre-serialize', [this, options, veto]);
    if (veto.veto) {
        log('ajaxSubmit: submit vetoed via form-pre-serialize trigger');
        return this;
    }

    // provide opportunity to alter form data before it is serialized
    if (options.beforeSerialize && options.beforeSerialize(this, options) === false) {
        log('ajaxSubmit: submit aborted via beforeSerialize callback');
        return this;
    }

    var a = this.formToArray(options.semantic);
    if (options.data) {
        options.extraData = options.data;
        for (var n in options.data) {
          if(options.data[n] instanceof Array) {
            for (var k in options.data[n])
              a.push( { name: n, value: options.data[n][k] } )
          }
          else
             a.push( { name: n, value: options.data[n] } );
        }
    }

    // give pre-submit callback an opportunity to abort the submit
    if (options.beforeSubmit && options.beforeSubmit(a, this, options) === false) {
        log('ajaxSubmit: submit aborted via beforeSubmit callback');
        return this;
    }

    // fire vetoable 'validate' event
    this.trigger('form-submit-validate', [a, this, options, veto]);
    if (veto.veto) {
        log('ajaxSubmit: submit vetoed via form-submit-validate trigger');
        return this;
    }

    var q = $.param(a);

    if (options.type.toUpperCase() == 'GET') {
        options.url += (options.url.indexOf('?') >= 0 ? '&' : '?') + q;
        options.data = null;  // data is null for 'get'
    }
    else
        options.data = q; // data is the query string for 'post'

    var $form = this, callbacks = [];
    if (options.resetForm) callbacks.push(function() { $form.resetForm(); });
    if (options.clearForm) callbacks.push(function() { $form.clearForm(); });

    // perform a load on the target only if dataType is not provided
    if (!options.dataType && options.target) {
        var oldSuccess = options.success || function(){};
        callbacks.push(function(data) {
            $(options.target).html(data).each(oldSuccess, arguments);
        });
    }
    else if (options.success)
        callbacks.push(options.success);

    options.success = function(data, status) {
        for (var i=0, max=callbacks.length; i < max; i++)
            callbacks[i].apply(options, [data, status, $form]);
    };

    // are there files to upload?
    var files = $('input:file', this).fieldValue();
    var found = false;
    for (var j=0; j < files.length; j++)
        if (files[j])
            found = true;

    // options.iframe allows user to force iframe mode
   if (options.iframe || found) {
       // hack to fix Safari hang (thanks to Tim Molendijk for this)
       // see:  http://groups.google.com/group/jquery-dev/browse_thread/thread/36395b7ab510dd5d
       if ($.browser.safari && options.closeKeepAlive)
           $.get(options.closeKeepAlive, fileUpload);
       else
           fileUpload();
       }
   else
       $.ajax(options);

    // fire 'notify' event
    this.trigger('form-submit-notify', [this, options]);
    return this;


    // private function for handling file uploads (hat tip to YAHOO!)
    function fileUpload() {
        var form = $form[0];

        if ($(':input[@name=submit]', form).length) {
            alert('Error: Form elements must not be named "submit".');
            return;
        }

        var opts = $.extend({}, $.ajaxSettings, options);
		var s = jQuery.extend(true, {}, $.extend(true, {}, $.ajaxSettings), opts);

        var id = 'jqFormIO' + (new Date().getTime());
        var $io = $('<iframe id="' + id + '" name="' + id + '" />');
        var io = $io[0];

        if ($.browser.msie || $.browser.opera)
            io.src = 'javascript:false;document.write("");';
        $io.css({ position: 'absolute', top: '-1000px', left: '-1000px' });

        var xhr = { // mock object
            aborted: 0,
            responseText: null,
            responseXML: null,
            status: 0,
            statusText: 'n/a',
            getAllResponseHeaders: function() {},
            getResponseHeader: function() {},
            setRequestHeader: function() {},
            abort: function() {
                this.aborted = 1;
                $io.attr('src','about:blank'); // abort op in progress
            }
        };

        var g = opts.global;
        // trigger ajax global events so that activity/block indicators work like normal
        if (g && ! $.active++) $.event.trigger("ajaxStart");
        if (g) $.event.trigger("ajaxSend", [xhr, opts]);

		if (s.beforeSend && s.beforeSend(xhr, s) === false) {
			s.global && jQuery.active--;
			return;
        }
        if (xhr.aborted)
            return;

        var cbInvoked = 0;
        var timedOut = 0;

        // add submitting element to data if we know it
        var sub = form.clk;
        if (sub) {
            var n = sub.name;
            if (n && !sub.disabled) {
                options.extraData = options.extraData || {};
                options.extraData[n] = sub.value;
                if (sub.type == "image") {
                    options.extraData[name+'.x'] = form.clk_x;
                    options.extraData[name+'.y'] = form.clk_y;
                }
            }
        }

        // take a breath so that pending repaints get some cpu time before the upload starts
        setTimeout(function() {
            // make sure form attrs are set
            var t = $form.attr('target'), a = $form.attr('action');
            $form.attr({
                target:   id,
                method:   'POST',
                action:   opts.url
            });

            // ie borks in some cases when setting encoding
            if (! options.skipEncodingOverride) {
                $form.attr({
                    encoding: 'multipart/form-data',
                    enctype:  'multipart/form-data'
                });
            }

            // support timout
            if (opts.timeout)
                setTimeout(function() { timedOut = true; cb(); }, opts.timeout);

            // add "extra" data to form if provided in options
            var extraInputs = [];
            try {
                if (options.extraData)
                    for (var n in options.extraData)
                        extraInputs.push(
                            $('<input type="hidden" name="'+n+'" value="'+options.extraData[n]+'" />')
                                .appendTo(form)[0]);

                // add iframe to doc and submit the form
                $io.appendTo('body');
                io.attachEvent ? io.attachEvent('onload', cb) : io.addEventListener('load', cb, false);
                form.submit();
            }
            finally {
                // reset attrs and remove "extra" input elements
                $form.attr('action', a);
                t ? $form.attr('target', t) : $form.removeAttr('target');
                $(extraInputs).remove();
            }
        }, 10);

        function cb() {
            if (cbInvoked++) return;

            io.detachEvent ? io.detachEvent('onload', cb) : io.removeEventListener('load', cb, false);

            var operaHack = 0;
            var ok = true;
            try {
                if (timedOut) throw 'timeout';
                // extract the server response from the iframe
                var data, doc;

                doc = io.contentWindow ? io.contentWindow.document : io.contentDocument ? io.contentDocument : io.document;

                if (doc.body == null && !operaHack && $.browser.opera) {
                    // In Opera 9.2.x the iframe DOM is not always traversable when
                    // the onload callback fires so we give Opera 100ms to right itself
                    operaHack = 1;
                    cbInvoked--;
                    setTimeout(cb, 100);
                    return;
                }

                xhr.responseText = doc.body ? doc.body.innerHTML : null;
                xhr.responseXML = doc.XMLDocument ? doc.XMLDocument : doc;
                xhr.getResponseHeader = function(header){
                    var headers = {'content-type': opts.dataType};
                    return headers[header];
                };

                if (opts.dataType == 'json' || opts.dataType == 'script') {
                    var ta = doc.getElementsByTagName('textarea')[0];
                    xhr.responseText = ta ? ta.value : xhr.responseText;
                }
                else if (opts.dataType == 'xml' && !xhr.responseXML && xhr.responseText != null) {
                    xhr.responseXML = toXml(xhr.responseText);
                }
                data = $.httpData(xhr, opts.dataType);
            }
            catch(e){
                ok = false;
                $.handleError(opts, xhr, 'error', e);
            }

            // ordering of these callbacks/triggers is odd, but that's how $.ajax does it
            if (ok) {
                opts.success(data, 'success');
                if (g) $.event.trigger("ajaxSuccess", [xhr, opts]);
            }
            if (g) $.event.trigger("ajaxComplete", [xhr, opts]);
            if (g && ! --$.active) $.event.trigger("ajaxStop");
            if (opts.complete) opts.complete(xhr, ok ? 'success' : 'error');

            // clean up
            setTimeout(function() {
                $io.remove();
                xhr.responseXML = null;
            }, 100);
        };

        function toXml(s, doc) {
            if (window.ActiveXObject) {
                doc = new ActiveXObject('Microsoft.XMLDOM');
                doc.async = 'false';
                doc.loadXML(s);
            }
            else
                doc = (new DOMParser()).parseFromString(s, 'text/xml');
            return (doc && doc.documentElement && doc.documentElement.tagName != 'parsererror') ? doc : null;
        };
    };
};

/**
 * ajaxForm() provides a mechanism for fully automating form submission.
 *
 * The advantages of using this method instead of ajaxSubmit() are:
 *
 * 1: This method will include coordinates for <input type="image" /> elements (if the element
 *    is used to submit the form).
 * 2. This method will include the submit element's name/value data (for the element that was
 *    used to submit the form).
 * 3. This method binds the submit() method to the form for you.
 *
 * The options argument for ajaxForm works exactly as it does for ajaxSubmit.  ajaxForm merely
 * passes the options argument along after properly binding events for submit elements and
 * the form itself.
 */
$.fn.ajaxForm = function(options) {
    return this.ajaxFormUnbind().bind('submit.form-plugin',function() {
        $(this).ajaxSubmit(options);
        return false;
    }).each(function() {
        // store options in hash
        $(":submit,input:image", this).bind('click.form-plugin',function(e) {
            var form = this.form;
            form.clk = this;
            if (this.type == 'image') {
                if (e.offsetX != undefined) {
                    form.clk_x = e.offsetX;
                    form.clk_y = e.offsetY;
                } else if (typeof $.fn.offset == 'function') { // try to use dimensions plugin
                    var offset = $(this).offset();
                    form.clk_x = e.pageX - offset.left;
                    form.clk_y = e.pageY - offset.top;
                } else {
                    form.clk_x = e.pageX - this.offsetLeft;
                    form.clk_y = e.pageY - this.offsetTop;
                }
            }
            // clear form vars
            setTimeout(function() { form.clk = form.clk_x = form.clk_y = null; }, 10);
        });
    });
};

// ajaxFormUnbind unbinds the event handlers that were bound by ajaxForm
$.fn.ajaxFormUnbind = function() {
    this.unbind('submit.form-plugin');
    return this.each(function() {
        $(":submit,input:image", this).unbind('click.form-plugin');
    });

};

/**
 * formToArray() gathers form element data into an array of objects that can
 * be passed to any of the following ajax functions: $.get, $.post, or load.
 * Each object in the array has both a 'name' and 'value' property.  An example of
 * an array for a simple login form might be:
 *
 * [ { name: 'username', value: 'jresig' }, { name: 'password', value: 'secret' } ]
 *
 * It is this array that is passed to pre-submit callback functions provided to the
 * ajaxSubmit() and ajaxForm() methods.
 */
$.fn.formToArray = function(semantic) {
    var a = [];
    if (this.length == 0) return a;

    var form = this[0];
    var els = semantic ? form.getElementsByTagName('*') : form.elements;
    if (!els) return a;
    for(var i=0, max=els.length; i < max; i++) {
        var el = els[i];
        var n = el.name;
        if (!n) continue;

        if (semantic && form.clk && el.type == "image") {
            // handle image inputs on the fly when semantic == true
            if(!el.disabled && form.clk == el)
                a.push({name: n+'.x', value: form.clk_x}, {name: n+'.y', value: form.clk_y});
            continue;
        }

        var v = $.fieldValue(el, true);
        if (v && v.constructor == Array) {
            for(var j=0, jmax=v.length; j < jmax; j++)
                a.push({name: n, value: v[j]});
        }
        else if (v !== null && typeof v != 'undefined')
            a.push({name: n, value: v});
    }

    if (!semantic && form.clk) {
        // input type=='image' are not found in elements array! handle them here
        var inputs = form.getElementsByTagName("input");
        for(var i=0, max=inputs.length; i < max; i++) {
            var input = inputs[i];
            var n = input.name;
            if(n && !input.disabled && input.type == "image" && form.clk == input)
                a.push({name: n+'.x', value: form.clk_x}, {name: n+'.y', value: form.clk_y});
        }
    }
    return a;
};

/**
 * Serializes form data into a 'submittable' string. This method will return a string
 * in the format: name1=value1&amp;name2=value2
 */
$.fn.formSerialize = function(semantic) {
    //hand off to jQuery.param for proper encoding
    return $.param(this.formToArray(semantic));
};

/**
 * Serializes all field elements in the jQuery object into a query string.
 * This method will return a string in the format: name1=value1&amp;name2=value2
 */
$.fn.fieldSerialize = function(successful) {
    var a = [];
    this.each(function() {
        var n = this.name;
        if (!n) return;
        var v = $.fieldValue(this, successful);
        if (v && v.constructor == Array) {
            for (var i=0,max=v.length; i < max; i++)
                a.push({name: n, value: v[i]});
        }
        else if (v !== null && typeof v != 'undefined')
            a.push({name: this.name, value: v});
    });
    //hand off to jQuery.param for proper encoding
    return $.param(a);
};

/**
 * Returns the value(s) of the element in the matched set.  For example, consider the following form:
 *
 *  <form><fieldset>
 *      <input name="A" type="text" />
 *      <input name="A" type="text" />
 *      <input name="B" type="checkbox" value="B1" />
 *      <input name="B" type="checkbox" value="B2"/>
 *      <input name="C" type="radio" value="C1" />
 *      <input name="C" type="radio" value="C2" />
 *  </fieldset></form>
 *
 *  var v = $(':text').fieldValue();
 *  // if no values are entered into the text inputs
 *  v == ['','']
 *  // if values entered into the text inputs are 'foo' and 'bar'
 *  v == ['foo','bar']
 *
 *  var v = $(':checkbox').fieldValue();
 *  // if neither checkbox is checked
 *  v === undefined
 *  // if both checkboxes are checked
 *  v == ['B1', 'B2']
 *
 *  var v = $(':radio').fieldValue();
 *  // if neither radio is checked
 *  v === undefined
 *  // if first radio is checked
 *  v == ['C1']
 *
 * The successful argument controls whether or not the field element must be 'successful'
 * (per http://www.w3.org/TR/html4/interact/forms.html#successful-controls).
 * The default value of the successful argument is true.  If this value is false the value(s)
 * for each element is returned.
 *
 * Note: This method *always* returns an array.  If no valid value can be determined the
 *       array will be empty, otherwise it will contain one or more values.
 */
$.fn.fieldValue = function(successful) {
    for (var val=[], i=0, max=this.length; i < max; i++) {
        var el = this[i];
        var v = $.fieldValue(el, successful);
        if (v === null || typeof v == 'undefined' || (v.constructor == Array && !v.length))
            continue;
        v.constructor == Array ? $.merge(val, v) : val.push(v);
    }
    return val;
};

/**
 * Returns the value of the field element.
 */
$.fieldValue = function(el, successful) {
    var n = el.name, t = el.type, tag = el.tagName.toLowerCase();
    if (typeof successful == 'undefined') successful = true;

    if (successful && (!n || el.disabled || t == 'reset' || t == 'button' ||
        (t == 'checkbox' || t == 'radio') && !el.checked ||
        (t == 'submit' || t == 'image') && el.form && el.form.clk != el ||
        tag == 'select' && el.selectedIndex == -1))
            return null;

    if (tag == 'select') {
        var index = el.selectedIndex;
        if (index < 0) return null;
        var a = [], ops = el.options;
        var one = (t == 'select-one');
        var max = (one ? index+1 : ops.length);
        for(var i=(one ? index : 0); i < max; i++) {
            var op = ops[i];
            if (op.selected) {
                // extra pain for IE...
                var v = $.browser.msie && !(op.attributes['value'].specified) ? op.text : op.value;
                if (one) return v;
                a.push(v);
            }
        }
        return a;
    }
    return el.value;
};

/**
 * Clears the form data.  Takes the following actions on the form's input fields:
 *  - input text fields will have their 'value' property set to the empty string
 *  - select elements will have their 'selectedIndex' property set to -1
 *  - checkbox and radio inputs will have their 'checked' property set to false
 *  - inputs of type submit, button, reset, and hidden will *not* be effected
 *  - button elements will *not* be effected
 */
$.fn.clearForm = function() {
    return this.each(function() {
        $('input,select,textarea', this).clearFields();
    });
};

/**
 * Clears the selected form elements.
 */
$.fn.clearFields = $.fn.clearInputs = function() {
    return this.each(function() {
        var t = this.type, tag = this.tagName.toLowerCase();
        if (t == 'text' || t == 'password' || tag == 'textarea')
            this.value = '';
        else if (t == 'checkbox' || t == 'radio')
            this.checked = false;
        else if (tag == 'select')
            this.selectedIndex = -1;
    });
};

/**
 * Resets the form data.  Causes all form elements to be reset to their original value.
 */
$.fn.resetForm = function() {
    return this.each(function() {
        // guard against an input with the name of 'reset'
        // note that IE reports the reset function as an 'object'
        if (typeof this.reset == 'function' || (typeof this.reset == 'object' && !this.reset.nodeType))
            this.reset();
    });
};

/**
 * Enables or disables any matching elements.
 */
$.fn.enable = function(b) {
    if (b == undefined) b = true;
    return this.each(function() {
        this.disabled = !b;
    });
};

/**
 * Checks/unchecks any matching checkboxes or radio buttons and
 * selects/deselects and matching option elements.
 */
$.fn.selected = function(select) {
    if (select == undefined) select = true;
    return this.each(function() {
        var t = this.type;
        if (t == 'checkbox' || t == 'radio')
            this.checked = select;
        else if (this.tagName.toLowerCase() == 'option') {
            var $sel = $(this).parent('select');
            if (select && $sel[0] && $sel[0].type == 'select-one') {
                // deselect all other options
                $sel.find('option').selected(false);
            }
            this.selected = select;
        }
    });
};

// helper fn for console logging
// set $.fn.ajaxSubmit.debug to true to enable debug logging
function log() {
    if ($.fn.ajaxSubmit.debug && window.console && window.console.log)
        window.console.log('[jquery.form] ' + Array.prototype.join.call(arguments,''));
};

})(jQuery);
/*
 * jqModal - Minimalist Modaling with jQuery
 *
 * Copyright (c) 2007 Brice Burgess <bhb@iceburg.net>, http://www.iceburg.net
 * Licensed under the MIT License:
 * http://www.opensource.org/licenses/mit-license.php
 *
 * $Version: 2007.??.?? +r12 beta
 * Requires: jQuery 1.1.3+
 */
(function($) {
    /**
     * Initialize a set of elements as "modals". Modals typically are popup dialogs,
     * notices, modal windows, and image containers. An expando ("_jqm") containing
     * the UUID or "serial" of the modal is added to each element. This expando helps
     * reference the modal's settings in the jqModal Hash Object (jQuery.jqm.hash)
     *
     * Accepts a parameter object with the following modal settings;
     *
     * (Integer) zIndex - Desired z-Index of the modal. This setting does not override (has no effect on) preexisting z-Index styling (set via CSS or inline style).
     * (Integer) overlay - [0-100] Translucency percentage (opacity) of the body covering overlay. Set to 0 for NO overlay, and up to 100 for a 100% opaque overlay.
     * (String) overlayClass - This class is applied to the body covering overlay. Allows CSS control of the overlay look (tint, background image, etc.).
     * (String) closeClass - A close trigger is added to all elements matching this class within the modal.
     * (Mixed) trigger - An open trigger is added to all matching elements within the DOM. Trigger can be a selector String, a jQuery collection of elements, a DOM element, or a False boolean.
     * (Mixed) ajax - If not false; The URL (string) to load content from via an AJAX request.
     *                If ajax begins with a "@", the URL is extracted from the requested attribute of the triggering element.
     * (Mixed) target - If not false; The element within the modal to load the ajax response (content) into. Allows retention of modal design (e.g. framing and close elements are not overwritten by the AJAX response).
     *                  Target may be a selector string, jQuery collection of elements, or a DOM element -- but MUST exist within (as a child of) the modal.
     * (Boolean) modal - If true, user interactivity will be locked to the modal window until closed.
     * (Boolean) toTop - If true, modal will be posistioned as a first child of the BODY element when opened, and its DOM posistion restored when closed. This aids in overcoming z-Index stacking order/containment issues where overlay covers whole page *including* modal.
     * (Mixed) onShow - User defined callback function fired when modal opened.
     * (Mixed) onHide - User defined callback function fired when modal closed.
     * (Mixed) onLoad - User defined callback function fired when ajax content loads.
     *
     * @name jqm
     * @param Map options User defined settings for the modal(s).
     * @cat Plugins/jqModal
     */
    $.fn.jqm = function(p) {
        var o = {
            zIndex: 3000,
            overlay: 40,
            overlayClass: 'jqmOverlay',
            closeClass: 'jqmClose',
            trigger: '.jqModal',
            ajax: false,
            target: false,
            modal: false,
            toTop: false,
            onShow: false,
            onHide: false,
            onLoad: false,
            resetOnClose: true
        };

        // For each element (aka "modal") $.jqm() has been called on;
        //  IF the _jqm expando exists, return (do nothing)
        //  ELSE increment serials and add _jqm expando to element ("serialization")
        //    *AND*...
        return this.each(function() {
            if (this._jqm)return;
            s++;
            this._jqm = s;

            // ... Add this element's serial to the jqModal Hash Object
            //  Hash is globally accessible via jQuery.jqm.hash. It consists of;
            //   c: {obj} config/options
            //   a: {bool} active state (true: active/visible, false: inactive/hidden)
            //   w: {JQ DOM Element} The modal element (window/dialog/notice/etc. container)
            //   s: {int} The serial number of this modal (same as "H[s].w[0]._jqm")
            //   t: {DOM Element} The triggering element
            // *AND* ...
            H[s] = {c:$.extend(o, p),a:false,w:$(this).addClass('jqmID' + s),s:s};

            // ... Attach events to trigger showing of this modal
            o.trigger && $(this).jqmAddTrigger(o.trigger);
            // -------------------------------------------------------------------------------------------------
            // Patch: added jqDrag support to make this modal draggable. jqDrag classname is hardcoded as of now
            // -------------------------------------------------------------------------------------------------
            $(this).jqDrag('.jqDrag');
        });
    };

    // Adds behavior to triggering elements via the hide-show (HS) function.
    //
    $.fn.jqmAddClose = function(e) {return HS(this, e, 'jqmHide');};
    $.fn.jqmAddTrigger = function(e) {return HS(this, e, 'jqmShow');};

    // Hide/Show a modal -- first check if it is already shown or hidden via the toggle state (H[{modal serial}].a)
    $.fn.jqmShow = function(t) {return this.each(function() {!H[this._jqm].a && $.jqm.open(this._jqm, t);});};
    $.fn.jqmHide = function(t) {return this.each(function() {H[this._jqm].a && $.jqm.close(this._jqm, t);});};

    $.jqm = {
        hash:{},

        // Function is executed by $.jqmShow to show a modal
        // s: {INT} serial of modal
        // t: {DOM Element} the triggering element

        // set local shortcuts
        //  h: {obj} this Modal's "hash"
        //  c: {obj} (h.c) config/options
        //  cc: {STR} closing class ('.'+h.c.closeClass)
        //  z: {INT} z-Index of Modal. If the Modal (h.w) has the z-index style set it will use this value before defaulting to the one passed in the config (h.c.zIndex)
        //  o: The overlay object
        // mark this modal as active (h.a === true)
        // set the triggering object (h.t) and the modal's z-Index.
        open:function(s, t) {
            var h = H[s],c = h.c,cc = '.' + c.closeClass,z = /^\d+$/.test(h.w.css('z-index')) && h.w.css('z-index') || c.zIndex,o = $('<div></div>').css({height:'100%',width:'100%',position:'fixed',left:0,top:0,'z-index':z - 1,opacity:c.overlay / 100});
            h.t = t;
            h.a = true;
            h.w.css('z-index', z);

            // IF the modal argument was passed as true;
            //    Bind the Keep Focus Function if no other Modals are open (!A[0]),
            //    Add this modal to the opened modals stack (A) for nested modal support,
            //    and Mark overlay to show wait cursor when mouse hovers over it.
            if (c.modal) {
                !A[0] && F('bind');
                A.push(s);
                o.css('cursor', 'wait');
            }

            // ELSE IF an overlay was requested (translucency set greater than 0);
            //    Attach a Close event to overlay to hide modal when overlay is clicked.
            else if (c.overlay > 0)h.w.jqmAddClose(o);

            // ELSE disable the overlay
            else o = false;

            if (!c.modal) {
              // Esc key should close window if modal is false. if modal is true, user action is required
              $(document).keydown( function( e ) {
                if( e.which == 27) {  // escape, close box
                  h.w.jqmHide();
                }
              });
            }

            // Add the Overlay to BODY if not disabled.
            h.o = (o) ? o.addClass(c.overlayClass).prependTo('body') : false;

            // IF IE6;
            //  Set the Overlay to 100% height/width, and fix-position it via JS workaround
            if (ie6 && $('html,body').css({height:'100%',width:'100%'}) && o) {
                o = o.css({position:'absolute'})[0];
                for (var y in {Top:1,Left:1})o.style.setExpression(y.toLowerCase(), "(_=(document.documentElement.scroll" + y + " || document.body.scroll" + y + "))+'px'");
            }

            // IF the modal's content is to be loaded via ajax;
            //  determine the target element {JQ} to recieve content (r),
            //  determine the URL {STR} to load content from (u)
            if (c.ajax) {
                var r = c.target || h.w,u = c.ajax,r = (typeof r == 'string') ? $(r, h.w) : $(r),u = (u.substr(0, 1) == '@') ? $(t).attr(u.substring(1)) : u;

                // Load the Content (and once loaded);
                // Fire the onLoad callback (if exists),
                // Attach closing events to elements inside the modal that match the closingClass,
                // and Execute the jqModal default Open Callback
                // -------------------------------------------------------------------------------------------------
                // Patch: added jqDrag support to make this modal draggable. jqDrag classname is hardcoded as of now
                // -------------------------------------------------------------------------------------------------
                r.load(u, function() {
                    $(this).jqDrag('.jqDrag');
                    c.onLoad && c.onLoad.call(this, h);
                    cc && h.w.jqmAddClose($(cc, h.w));
                    O(h);
                });
            }

            // ELSE the modal content is NOT to be loaded via ajax;
            //  Attach closing events to elements inside the modal that match the closingClass
            else cc && h.w.jqmAddClose($(cc, h.w));

            // IF toTop was passed and an overlay exists;
            //  Remember the DOM posistion of the modal by inserting a tagged (matching serial) <SPAN> before the modal
            //  Move the Modal from its current position to a first child of the body tag (after the overlay)
            c.toTop && h.o && h.w.before('<span id="jqmP' + h.w[0]._jqm + '"></span>').insertAfter(h.o);

            // Execute user defined onShow callback, or else show (make visible) the modal.
            // Execute the jqModal default Open Callback.
            // Return false to prevent trigger click from being followed.
            (c.onShow) ? c.onShow(h) : h.w.show();
            O(h);
            return false;

        },

        // Function is executed by $.jqmHide to hide a modal
        // mark this modal as inactive (h.a === false)
        close:function(s, t) {
            var h = H[s];
            h.cl=t;
            h.a = false;
            // If modal, remove from modal stack.
          
            // If no modals in modal stack, unbind the Keep Focus Function
            if (h.c.modal) {
                A.pop();
                !A[0] && F('unbind');
            }

            // IF toTop was passed and an overlay exists;
            //  Move modal back to its previous ("remembered") position.
            h.c.toTop && h.o && $('#jqmP' + h.w[0]._jqm).after(h.w).remove();

            // Execute user defined onHide callback, or else hide (make invisible) the modal and remove the overlay.
            if (h.c.onHide)h.c.onHide(h); else {h.w.hide() && h.o && h.o.remove();}

            if (h.c.resetOnClose) {
              // Kani Patch: this will reset the position before closing. so the dialogue box will not open at the previous
              // position. this is needed because in IE the div is absolutely positioned
              // this behaviour can be overridden by setting resetOnClose to false (defaults to true).
              // need to do this in modals which show a message inside the modal itself before closing.
              h.w.css('top', '');
              h.w.css('left', '');
            }
            return false;
        }};

    // set jqModal scope shortcuts;
    //  s: {INT} serials placeholder
    //  H: {HASH} shortcut to jqModal Hash Object
    //  A: {ARRAY} Array of active/visible modals
    //  ie6: {bool} True if client browser is Internet Explorer 6
    //  i: {JQ, DOM Element} iframe placeholder used to prevent active-x bleedthrough in IE6
    //    NOTE: It is important to include the iframe styling (iframe.jqm) in your CSS!
    //     *AND* ...
    var s = 0,H = $.jqm.hash,A = [],ie6 = $.browser.msie && ($.browser.version == "6.0"),i = $('<iframe src="javascript:false;document.write(\'\');" class="jqm"></iframe>').css({opacity:0}),

        //  O: The jqModal default Open Callback;
        //    IF ie6; Add the iframe to the overlay (if overlay exists) OR to the modal (if an iframe doesn't already exist from a previous opening)
        //    Execute the Modal Focus Function
            O = function(h) {
                if (ie6)h.o && h.o.html('<p style="width:100%;height:100%"/>').prepend(i) || (!$('iframe.jqm', h.w)[0] && h.w.prepend(i));
                f(h);
            },

        //  f: The Modal Focus Function;
        //    Attempt to focus the first visible input within the modal
            f = function(h) {try {$(':input:visible', h.w)[0].focus();} catch(e) {}},

        //  F: The Keep Focus Function;
        //    Binds or Unbinds (t) the Focus Examination Function to keypresses and clicks
            F = function(t) {$()[t]("keypress", x)[t]("keydown", x)[t]("mousedown", x);},

        //  x: The Focus Examination Function;
        //    Fetch the current modal's Hash as h (supports nested modals)
        //    Determine if the click/press falls within the modal. If not (r===true);
        //      call the Modal Focus Function and prevent click/press follow-through (return false [!true])
        //      ELSE if so (r===false); follow event (return true [!false])
            x = function(e) {
                var h = H[A[A.length - 1]],r = (!$(e.target).parents('.jqmID' + h.s)[0]);
                r && f(h);
                return !r;
            },

        // hide-show function; assigns click events to trigger elements that
        //   hide, show, or hide AND show modals.

        // Expandos (jqmShow and/or jqmHide) are added to all trigger elements.
        // These Expandos hold an array of modal serials {INT} to show or hide.

        //  w: {DOM Element} The modal element (window/dialog/notice/etc. container)
        //  e: {DOM Elemet||jQ Selector String} The triggering element
        //  y: {String} Type (jqmHide||jqmShow)

        //  s: {array} the serial number of passed modals, calculated below;
            HS = function(w, e, y) {
                var s = [];
                w.each(function() {s.push(this._jqm);});

                // for each triggering element attach the jqmHide or jqmShow expando (y)
                //  or else expand the expando with the current serial array
                $(e).each(function() {
                    if (this[y])$.extend(this[y], s);

                    // Assign a click event on the trigger element which examines the element's
                    //  jqmHide/Show expandos and attempts to execute $.jqmHide/Show on matching modals
                    else {
                        this[y] = s;
                        $(this).click(function() {
                            for (var i in {jqmShow:1,jqmHide:1})for (var s in this[i])if (H[this[i][s]])H[this[i][s]].w[i](this);
                            return false;
                        });
                    }
                });
                return w;
            };
})(jQuery);
/*
 * jqDnR - Minimalistic Drag'n'Resize for jQuery.
 *
 * Copyright (c) 2007 Brice Burgess <bhb@iceburg.net>, http://www.iceburg.net
 * Licensed under the MIT License:
 * http://www.opensource.org/licenses/mit-license.php
 *
 * $Version: 2007.08.19 +r2
 */

(function($){
$.fn.jqDrag=function(h){return i(this,h,'d');};
$.fn.jqResize=function(h){return i(this,h,'r');};
$.jqDnR={dnr:{},e:0,
drag:function(v){
 if(M.k == 'd')E.css({left:M.X+v.pageX-M.pX,top:M.Y+v.pageY-M.pY});
 else
E.css({width:Math.max(v.pageX-M.pX+M.W,0),height:Math.max(v.pageY-M.pY+M.H,0)});
  return false;},
stop:function(){/*E.css('opacity',M.o);*/$().unbind('mousemove',J.drag).unbind('mouseup',J.stop);}
};
var J=$.jqDnR,M=J.dnr,E=J.e,
i=function(e,h,k){return e.each(function(){h=(h)?$(h,e):e;
 h.bind('mousedown',{e:e,k:k},function(v){var d=v.data,p={},vertScroll=0;E=d.e;
 // attempt utilization of dimensions plugin to fix IE issues
 if(E.css('position') != 'relative'){if (E.css('position') != 'absolute') {vertScroll=$(window).scrollTop();};p=E.position();}
M={X:p.left||f('left')||0,Y:(p.top-vertScroll)||f('top')||0,W:f('width')||E[0].scrollWidth||0,H:f('height')||E[0].scrollHeight||0,pX:v.pageX,pY:v.pageY,k:d.k,o:E.css('opacity')};
 /*E.css({opacity:0.8})*/;$().mousemove($.jqDnR.drag).mouseup($.jqDnR.stop);
 return false;
 });
});},
f=function(k){return parseInt(E.css(k))||false;};
})(jQuery);
function getErrorHtmlFromJsonResponse(response) {
  var html = '<div class="errorContainer"><p>Please fix the following errors:</p><ol class="errorList">';
  for (var errKey in response.data) {
    html += "<li class='errorMessage'>" + response.data[errKey] + "</li>";
  }
  html += "</ol></div>";
  return html;
}
/*
 * this file contains the following plugins
 * slowFade, stepper, labelify, form, jqModal (along with jq drag'n'resize), getErrorHtmlFromJsonResponse
 */
// extend jquery for slowFade effect. not making separate plugin as this is used everywhere

/*
 * DropMenu.js
 * for the healthkart top menu bar
 */


jQuery.fn.hoverBox = function(targetHoverBox, reposition) {
  //If the mouse is on neither source link nor target then fadeOut the element
  function checkMenuHover(element, obj, tar) {
    if (!(obj) && !(tar)) {
      $(element).stop(true, true).fadeOut();
      /*Remove any queued animations and jump to the target CSS property before starting another animation*/
    }
  }

  var obj = null; //To test if mouse is on the source link
  var tar = null; //To test if mouse is on the drop menu
  var element = null; //The target. Needed because it can either address all targets on the page or just the local target (sibling to the source)
  var tarExist = false;  //To test if the user hovered over a child drop menu
  var t; //Timer

  $(this).hover(function(e) {
    // Clear any prior setTimeout functions, hide any existing drop menus, and reset tarExist
    clearTimeout(t);
    checkMenuHover(targetHoverBox, obj,tar);
    tarExist = false;

    //Show the child drop menu for this link
    element = $(this).parent().find(targetHoverBox);
    if (reposition) {
      var offset = $(this).offset();
      var width = $(this).width();
      var offsetContainer = $('#container').offset();
      //Needed since the hoverbox will be positioned relative to the container
      var tLeft = offset.left - offsetContainer.left + width;
      var tTop = offset.top - offsetContainer.top;
      element.css({'top' : tTop, 'left' : tLeft});
//      element.css({'top' : tTop - 50 + "px", 'left' : tLeft + linkWidth - 200 + "px"});
    }
    element.stop(true, true).fadeIn();
    obj = $(this); //mouse on source
  }, function() {
    obj = null; //source reset
    $(targetHoverBox).hover(function() {
      tarExist = true; // target was hovered over
      tar = $(this); //mouse on target
    }, function() {
      tar = null; //target reset
      t = setTimeout(function(){checkMenuHover(element,obj,tar);}, 300);
    });
    if (!(tarExist)) {
      t = setTimeout(function(){checkMenuHover(element,obj,tar);}, 300); //A setTimeout call would already be in progress is tarExist was true, so only do it in case it's false
    }
  });
};

/**
* hoverIntent is similar to jQuery's built-in "hover" function except that
* instead of firing the onMouseOver event immediately, hoverIntent checks
* to see if the user's mouse has slowed down (beneath the sensitivity
* threshold) before firing the onMouseOver event.
*
* hoverIntent r6 // 2011.02.26 // jQuery 1.5.1+
* <http://cherne.net/brian/resources/jquery.hoverIntent.html>
*
* hoverIntent is currently available for use in all personal or commercial
* projects under both MIT and GPL licenses. This means that you can choose
* the license that best suits your project, and use it accordingly.
*
* // basic usage (just like .hover) receives onMouseOver and onMouseOut functions
* $("ul li").hoverIntent( showNav , hideNav );
*
* // advanced usage receives configuration object only
* $("ul li").hoverIntent({
*	sensitivity: 7, // number = sensitivity threshold (must be 1 or higher)
*	interval: 100,   // number = milliseconds of polling interval
*	over: showNav,  // function = onMouseOver callback (required)
*	timeout: 0,   // number = milliseconds delay before onMouseOut function call
*	out: hideNav    // function = onMouseOut callback (required)
* });
*
* @param  f  onMouseOver function || An object with configuration options
* @param  g  onMouseOut function  || Nothing (use configuration options object)
* @author    Brian Cherne brian(at)cherne(dot)net
*/
(function($) {
	$.fn.hoverIntent = function(f,g) {
		// default configuration options
		var cfg = {
			sensitivity: 7,
			interval: 100,
			timeout: 0
		};
		// override configuration options with user supplied object
		cfg = $.extend(cfg, g ? { over: f, out: g } : f );

		// instantiate variables
		// cX, cY = current X and Y position of mouse, updated by mousemove event
		// pX, pY = previous X and Y position of mouse, set by mouseover and polling interval
		var cX, cY, pX, pY;

		// A private function for getting mouse position
		var track = function(ev) {
			cX = ev.pageX;
			cY = ev.pageY;
		};

		// A private function for comparing current and previous mouse position
		var compare = function(ev,ob) {
			ob.hoverIntent_t = clearTimeout(ob.hoverIntent_t);
			// compare mouse positions to see if they've crossed the threshold
			if ( ( Math.abs(pX-cX) + Math.abs(pY-cY) ) < cfg.sensitivity ) {
				$(ob).unbind("mousemove",track);
				// set hoverIntent state to true (so mouseOut can be called)
				ob.hoverIntent_s = 1;
				return cfg.over.apply(ob,[ev]);
			} else {
				// set previous coordinates for next time
				pX = cX; pY = cY;
				// use self-calling timeout, guarantees intervals are spaced out properly (avoids JavaScript timer bugs)
				ob.hoverIntent_t = setTimeout( function(){compare(ev, ob);} , cfg.interval );
			}
		};

		// A private function for delaying the mouseOut function
		var delay = function(ev,ob) {
			ob.hoverIntent_t = clearTimeout(ob.hoverIntent_t);
			ob.hoverIntent_s = 0;
			return cfg.out.apply(ob,[ev]);
		};

		// A private function for handling mouse 'hovering'
		var handleHover = function(e) {
			// copy objects to be passed into t (required for event object to be passed in IE)
			var ev = jQuery.extend({},e);
			var ob = this;

			// cancel hoverIntent timer if it exists
			if (ob.hoverIntent_t) { ob.hoverIntent_t = clearTimeout(ob.hoverIntent_t); }

			// if e.type == "mouseenter"
			if (e.type == "mouseenter") {
				// set "previous" X and Y position based on initial entry point
				pX = ev.pageX; pY = ev.pageY;
				// update "current" X and Y position based on mousemove
				$(ob).bind("mousemove",track);
				// start polling interval (self-calling timeout) to compare mouse coordinates over time
				if (ob.hoverIntent_s != 1) { ob.hoverIntent_t = setTimeout( function(){compare(ev,ob);} , cfg.interval );}

			// else e.type == "mouseleave"
			} else {
				// unbind expensive mousemove event
				$(ob).unbind("mousemove",track);
				// if hoverIntent state is true, then call the mouseOut function after the specified delay
				if (ob.hoverIntent_s == 1) { ob.hoverIntent_t = setTimeout( function(){delay(ev,ob);} , cfg.timeout );}
			}
		};

		// bind the function to the two event listeners
		return this.bind('mouseenter',handleHover).bind('mouseleave',handleHover);
	};
})(jQuery);

(function($){
	$.fn.popupWindow = function(instanceSettings){

		return this.each(function(){

		$(this).click(function(){

		$.fn.popupWindow.defaultSettings = {
			centerBrowser:0, // center window over browser window? {1 (YES) or 0 (NO)}. overrides top and left
			centerScreen:0, // center window over entire screen? {1 (YES) or 0 (NO)}. overrides top and left
			height:500, // sets the height in pixels of the window.
			left:0, // left position when the window appears.
			location:0, // determines whether the address bar is displayed {1 (YES) or 0 (NO)}.
			menubar:0, // determines whether the menu bar is displayed {1 (YES) or 0 (NO)}.
			resizable:0, // whether the window can be resized {1 (YES) or 0 (NO)}. Can also be overloaded using resizable.
			scrollbars:1, // determines whether scrollbars appear on the window {1 (YES) or 0 (NO)}.
			status:0, // whether a status line appears at the bottom of the window {1 (YES) or 0 (NO)}.
			width:500, // sets the width in pixels of the window.
			windowName:null, // name of window set from the name attribute of the element that invokes the click
			windowURL:null, // url used for the popup
			top:0, // top position when the window appears.
			toolbar:0 // determines whether a toolbar (includes the forward and back buttons) is displayed {1 (YES) or 0 (NO)}.
		};

		settings = $.extend({}, $.fn.popupWindow.defaultSettings, instanceSettings || {});

		var windowFeatures =    'height=' + settings.height +
								',width=' + settings.width +
								',toolbar=' + settings.toolbar +
								',scrollbars=' + settings.scrollbars +
								',status=' + settings.status +
								',resizable=' + settings.resizable +
								',location=' + settings.location +
								',menuBar=' + settings.menubar;

				settings.windowName = this.name || settings.windowName;
				settings.windowURL = this.href || settings.windowURL;
				var centeredY,centeredX;

				if(settings.centerBrowser){

					if ($.browser.msie) {//hacked together for IE browsers
						centeredY = (window.screenTop - 120) + ((((document.documentElement.clientHeight + 120)/2) - (settings.height/2)));
						centeredX = window.screenLeft + ((((document.body.offsetWidth + 20)/2) - (settings.width/2)));
					}else{
						centeredY = window.screenY + (((window.outerHeight/2) - (settings.height/2)));
						centeredX = window.screenX + (((window.outerWidth/2) - (settings.width/2)));
					}
					window.open(settings.windowURL, settings.windowName, windowFeatures+',left=' + centeredX +',top=' + centeredY).focus();
				}else if(settings.centerScreen){
					centeredY = (screen.height - settings.height)/2;
					centeredX = (screen.width - settings.width)/2;
					window.open(settings.windowURL, settings.windowName, windowFeatures+',left=' + centeredX +',top=' + centeredY).focus();
				}else{
					window.open(settings.windowURL, settings.windowName, windowFeatures+',left=' + settings.left +',top=' + settings.top).focus();
				}
				return false;
			});

		});
	};
})(jQuery);

$(document).ready(function() {
  // heartbeat. keeps the user session from expiring.
  setInterval(heartbeat, 150000);
  $('#searchbox').labelify({labelledClass: 'input_tip'});

  var onerror = function() {
    if (this.getAttribute("onerror_cnt") == 0) {
      this.setAttribute("onerror_cnt", 1);
      $(this).attr("src", $('#imageNotFound').attr('src')).attr("width", 100);
    }
    return false;
  };

  $('.product_image').attr('onerror_cnt', 0).error(onerror);

});
function heartbeat() {
//  if ($('#heartbeat').size()>0) $.getJSON($('#heartbeat').attr('href'), function() {});
}
//Issues: Hover out gets called multiple times.
