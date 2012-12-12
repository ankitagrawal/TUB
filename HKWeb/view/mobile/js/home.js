$('#primaryCategory').bind('pagebeforeshow', function () {
	    jQuery.support.cors = true;
	    _.templateSettings = {
	        evaluate: /\{\{(.+?)\}\}/g
	    };
	    Backbone.emulateJSON = true;
	   
	    var CategoryModel = Backbone.Model.extend({
	        initialize: function () {
	            _.bindAll(this, 'render');
	            this.render();

	        },
	        render: function () {
	            var catView = new CategoryView({
	                model: this
	            });

	            $('#categoryContainer ul').append(catView.render().el);
	        }
	    });

	    var CategoryCollection = Backbone.Collection.extend({
	        model: CategoryModel,
	        initialize: function () {
	            _.bindAll(this, 'clearView');
	            this.clearView();
	            this.on('reset', this.clearView, this);

	        },
	        clearView: function () {
	            $('#categoryContainer ul').html('<li class=heading data-role="list-divider">CATEGORY</li>');
	        },
	        updateUI: function () {
	            $('#categoryContainer ul').listview();
	            $('#categoryContainer ul').listview('refresh');
	        }
	     });


	    var CategoryView = Backbone.View.extend({
	        tagName: 'li',
	        className: '',
	        initialize: function () {
	            _.bindAll(this, 'render');
	        },
	        template: _.template($('#categoryTemplate').html()),
	        render: function () {
	            $(this.el).empty();
	            $(this.el).html(this.template(this.model.toJSON()));
	            $(this.el).attr('data-icon', 'arr')
	            return this;
	        }
	    });

	    var catCol = new CategoryCollection();
	   
	    loadingPop('s', '');
	    
	    $.ajax({
	        url: wSURL + __hkG.urls.primaryCategory,
	        success: function (response) {
	            if (hasErr(response)) {
	                loadingPop('h');
	                alert(getErr(response.message));
	            } else {
	                if (response.data.length == 0 || response.data == null) {
	                    $('#categoryContainer ul').append('<h3 style="padding-left:20px">No Data Found!</h3>');
	                } else {
	                    if (catCol.add(response.data)) {
	                        catCol.updateUI();
	                    }
	                }
	                loadingPop('h');
	            }
	        },
	        error: function () {
	            alert(__hkG.errs.requestFail);
	            loadingPop('h');
	        }
	    });
	   
	});