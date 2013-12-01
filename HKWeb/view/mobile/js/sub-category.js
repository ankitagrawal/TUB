$("#sub-category").bind("pagebeforeshow",function(event, ui) {
				//$('div[id=product]').remove();
					var urlEval = new URLEval();
					var x = $.mobile.path.parseUrl(urlEval.getURLFromHash(location.href));
					var queryString = x.search;
					
					jQuery.support.cors = true;
					_.templateSettings = {
						evaluate : /\{\{(.+?)\}\}/g
					};
					
					Backbone.emulateJSON = true;
					
					var ProductScrollModel = Backbone.Model.extend({
						initialize : function() {
							_.bindAll(this, 'render');
							this.render();
						},
						render : function() {
							var prScVi = new ProductScrollView({
								model : this
							});
							$('#sub-category-scroll')
									.append(prScVi.render().el);
						}
					});

					var ProductScrollCollection = Backbone.Collection.extend({
						model : ProductScrollModel,
						initialize : function() {
							this.on('reset', this.clearView, this);
							this.clearView();
						},
						clearView : function() {
							$('#sub-category-scroll').html('');
						}
					});
					

					var ProductScrollView = Backbone.View.extend({
						tagName : 'table',
						className : 'scrollWindow',
						initialize : function() {
							_.bindAll(this, 'render');
						},
						template : _
								.template($('#product-fav-template')
										.html()),
						render : function() {
							$(this.el).empty();
							$(this.el).html(
									this.template(this.model.toJSON()));
							return this;
						}
					});

					/** ***Sub Category listing *S*** */
					var SubCategoryModel = Backbone.Model.extend({
						initialize : function() {
							_.bindAll(this, 'render');
							this.render();
						},
						render : function() {
							var suCaVi = new SubCategoryView({
								model : this
							});
							$('#subCategoryContainer ul').append(
									suCaVi.render().el);
						}
					});

					var SubCategoryCollection = Backbone.Collection
							.extend({
								model : SubCategoryModel,
								initialize : function() {
									_.bindAll(this, 'clearView');
									this.clearView();
									this.on('reset', this.clearView, this);
								},
								clearView : function() {
									
									$('#subCategoryContainer ul')
											.html(
													'<li class=heading data-role="list-divider">CATEGORY</li>');
								},
								updateUI : function() {
									$('#subCategoryContainer ul').listview();
									$('#subCategoryContainer ul').listview(
											'refresh');
								}

							});

					var SubCategoryView = Backbone.View
							.extend({
								tagName : 'li',
								className : '',
								initialize : function() {
									_.bindAll(this, 'render');
								},
								template : _.template($(
										'#sub-category-template').html()),
								render : function() {
									$(this.el).empty();
									$(this.el).html(
											this.template(this.model.toJSON()));
									$(this.el).attr('data-icon', 'arr')
									return this;
								}
							});
					/** ***Sub Category listing *E*** */

					var prScCo = new ProductScrollCollection();
				/*	if (prScCo.add({
						"favItem" : [ {
							"id" : 1,
							"label" : "Nutrition",
							"img" : "1.jpg"
						}, {
							"id" : 2,
							"label" : "Sports & Fitness",
							"img" : "2.jpg"
						}, {
							"id" : 3,
							"label" : "Diabetes",
							"img" : "3.jpg"
						}, {
							"id" : 4,
							"label" : "Home Devices",
							"img" : "4.jpg"
						}, {
							"id" : 5,
							"label" : "Eye",
							"img" : "5.jpg"
						}, {
							"id" : 6,
							"label" : "Personal Care",
							"img" : "6.jpg"
						}, {
							"id" : 7,
							"label" : "Beauty",
							"img" : "7.jpg"
						}, {
							"id" : 8,
							"label" : "Parenting",
							"img" : "8.jpg"
						}, {
							"id" : 9,
							"label" : "Services",
							"img" : "9.jpg"
						} ]
					})) {
						$('.scrollWindow').scrollWindow({
							scrollFactor : 35
						});
					}
					*/
					var suCaCo = new SubCategoryCollection();
					suCaCo.reset();
					loadingPop('s', '');

					$.ajax({
								url : wSURL + __hkG.urls.subCategory
										+ queryString,
								success : function(response) {
									if (hasErr(response)) {
										loadingPop('h');
										popUpMob.show(getErr(response.message));
									} else {
										if (response.data.length == 0
												|| response.data == null) {
											$('#subCategoryContainer ul')
													.append(
															'<h3 style="padding-left:20px">No Data Found!</h3>');
										} else {
											if (suCaCo.add(response.data)) {
												suCaCo.updateUI();
											}
										}
										loadingPop('h');
									}
								},
								error : function() {
									popUpMob.show(__hkG.errs.requestFail);
									loadingPop('h');
								}

							});

				});
