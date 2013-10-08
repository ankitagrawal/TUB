<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:layout-render name="/layouts/categoryBlankLanding.jsp"
                 pageTitle="Careers at HealthKart">

<s:layout-component name="htmlHead">
  <style type="text/css">

    ul, ol {
      margin: 0;
      padding: 0;
    }

    @font-face
    {
      font-family: 'whitney_htf_bookregular'
    ;
      src: url('${pageContext.request.contextPath}/css/whitneyhtf-book-webfont.eot')
    ;
      src: url('${pageContext.request.contextPath}/css/whitneyhtf-book-webfont.eot?#iefix') format('embedded-opentype'), url('${pageContext.request.contextPath}/css/whitneyhtf-book-webfont.woff') format('woff'), url('${pageContext.request.contextPath}/css/whitneyhtf-book-webfont.ttf') format('truetype'), url('${pageContext.request.contextPath}/css/whitneyhtf-book-webfont.svg#whitney_htf_bookregular') format('svg')
    ;
      font-weight: normal
    ;
      font-style: normal
    ;

    }

    @font-face
    {
      font-family: 'whitney_htf_scbold'
    ;
      src: url('${pageContext.request.contextPath}/css/whitneyhtf-boldsc-webfont.eot')
    ;
      src: url('${pageContext.request.contextPath}/css/whitneyhtf-boldsc-webfont.eot?#iefix') format('embedded-opentype'), url('${pageContext.request.contextPath}/css/whitneyhtf-boldsc-webfont.woff') format('woff'), url('${pageContext.request.contextPath}/css/whitneyhtf-boldsc-webfont.ttf') format('truetype'), url('${pageContext.request.contextPath}/css/whitneyhtf-boldsc-webfont.svg#whitney_htf_scbold') format('svg')
    ;
      font-weight: normal
    ;
      font-style: normal
    ;

    }
    @font-face
    {
      font-family: 'whitney_htf_mediumregular'
    ;
      src: url('${pageContext.request.contextPath}/css/whitneyhtf-medium-webfont.eot')
    ;
      src: url('${pageContext.request.contextPath}/css/whitneyhtf-medium-webfont.eot?#iefix') format('embedded-opentype'), url('${pageContext.request.contextPath}/css/whitneyhtf-medium-webfont.woff') format('woff'), url('${pageContext.request.contextPath}/css/whitneyhtf-medium-webfont.ttf') format('truetype'), url('${pageContext.request.contextPath}/css/whitneyhtf-medium-webfont.svg#whitney_htf_mediumregular') format('svg')
    ;
      font-weight: normal
    ;
      font-style: normal
    ;

    }
    #pageContainer {
      margin: 15px auto;
      width: 960px;
      overflow: auto;
      padding-bottom: 20px;
      font-family: 'whitney_htf_bookregular';
      color: #464646;
    }

    .cl {
      clear: both;
    }

    .headingMain {
      font-size: 28px;
      font-family: 'whitney_htf_bookregular';
      font-weight: bold;
      margin-bottom: 20px;
    }

    .headingMain span {
      font-weight: normal;
      margin-left: 20px;
    }

    .headBanner {
      font-size: 15px;
      line-height: 22px;
      font-family: 'whitney_htf_bookregular';
      font-weight: normal;
      margin: 20px 0;
    }

    .roles {
      float: left;
      width: 50px;
      font-size: 18px;
      font-family: 'whitney_htf_bookregular';
      padding-top: 13px;
      font-weight: bold;
    }

    .tab-container {
      float: right;
      width: 890px;
    }

    .etabs {
      margin: 0;
      padding: 0;
      font-family: 'whitney_htf_bookregular';
    }

    .tab {
      display: inline-block;
      zoom: 1;
      *display: inline;
    }

    .tab a {
      font-size: 18px;
      line-height: 2em;
      display: block;
      padding: 0 10px;
      outline: none;
      border-bottom: none;
      color: #464646;
    }

    .tab a:hover {
      text-decoration: none;
    }

    .tab.active {
      background: #fff;
      padding-top: 6px;
      position: relative;
      top: 1px;
      border-bottom: 2px solid #464646;
    }

    .tab a.active {
      font-weight: normal;
    }

    .tab-container .panel-container {
      background: #fff;
      padding: 10px;
    }

    .panel-container {
      margin-bottom: 10px;
      font-family: 'whitney_htf_bookregular';
      font-size: 15px;
    }

    .panel-container h3 {
      margin: 0;
      padding: 0;
      font-size: 17px;
      margin-top: 10px;
      font-weight: bold;
    }

    .panel-container p {
      margin-left: 0;
      padding-left: 0;
    }

    .panel-container ul {
      margin-left: 20px;
      font-size: 15px;
    }


  </style>

  <script src="${pageContext.request.contextPath}/js/jquery-1.7.1.min.js" type="text/javascript"></script>
  <script src="${pageContext.request.contextPath}/js/jquery.hashchange.min.js" type="text/javascript"></script>
  <script src="${pageContext.request.contextPath}/js/jquery.easytabs.min.js" type="text/javascript"></script>

  <script type="text/javascript">
    $(document).ready(function() {
      $('#tab-container').easytabs();
    });
  </script>

  <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.4.2/jquery.min.js"></script>
  <script type="text/javascript" src="${pageContext.request.contextPath}/js/ddpowerzoomer.js">

    /***********************************************
     * Image Power Zoomer- (c) Dynamic Drive DHTML code library (www.dynamicdrive.com)
     * This notice MUST stay intact for legal use
     * Visit Dynamic Drive at http://www.dynamicdrive.com/ for this script and 100s more
     ***********************************************/

  </script>

  <script type="text/javascript">
    jQuery(document).ready(function($) { //fire on DOM ready
      $('#myimage').addpowerzoom({
        defaultpower:4, //default power: 3x original image
        magnifiersize: [200, 200]
      })

    })
  </script>
</s:layout-component>

<s:layout-component name="header">
  <div></div>
</s:layout-component>

<s:layout-component name="menu">
  <div id="logoBoxContainer" style="cursor:default; width: 960px; margin: 5px auto 7px;">

    <div class='logoBox' style="float:left;">
      <s:link href="/" title='go to healthkart home'>
        <img src='<hk:vhostImage/>/images/logo.png' alt="healthkart logo"/>
      </s:link>
    </div>
    <div style="vertical-align:middle;padding-top:10px;">
      <h3 style="font-weight:normal;">Work@HealthKart never ends... but neither does all the fun</h3>
    </div>
    <div style="clear:both;"></div>
  </div>
</s:layout-component>



<%--<s:layout-component name="breadcrumbs">
  <div class='crumb_outer'>
    <s:link beanclass="com.hk.web.action.HomeAction" class="crumb">Home</s:link>
    &gt;
    <span class="crumb last" style="font-size: 12px;">Careers</span>

    <h1 class="title">
      Careers at HealthKart.com
    </h1>
  </div>

</s:layout-component>--%>

<s:layout-component
    name="metaDescription">Latest Openings at HealthKart.com! Send in your resumes now!</s:layout-component>
<s:layout-component name="metaKeywords"></s:layout-component>

<s:layout-component name="content">
<div class="cl"></div>
<div id="pageContainer">
<%--<div style="padding-bottom:15px;">
  <h3 style="font-weight:bold;">Work@HealthKart never ends... but neither does all the fun</h3>
</div>--%>
<img id="myimage" src="${pageContext.request.contextPath}/images/careers/banner-large.jpg" alt="" border="0"
     style="width:960px; height:265px;"/>

<div class="cl"></div>
<h3 class="headBanner">HealthKart is India's biggest health store on the internet. Our singular mission is to reach
  to provide access to authentic, quality, and affordable health products to those who don't. We strive to live up to
  this
  with an ever growing but comprehensive catalog and a delivery team that reaches more that 4000 places in
  India.
  <br/>
  If you believe that you have the spark which can engnite and propel HealthKart even futher on the growth path mail
  your CVs at
  <a href="mailto:jobs@healthkart.com" style="font-size:1.1em;">jobs@healthkart.com</a></h3>

<div class="cl"></div>

<div class="roles">Roles</div>
<div id="tab-container" class='tab-container'>
<ul class='etabs'>
  <li class='tab'><a href="#tech">Tech</a></li>
  <li class='tab'><a href="#design">Design</a></li>
  <li class='tab'><a href="#category">Category</a></li>
  <li class='tab'><a href="#cs">Customer Support</a></li>
  <li class='tab'><a href="#analytics">Analytics</a></li>
  <li class='tab'><a href="#sales">Sales</a></li>
</ul>
<div class='panel-container'>

<div id="analytics">
  <h3>Position: Analytics Manager</h3>

  <p>
    Seeking a result oriented individual to lead and drive analytics infrastructure and roadmap of HealthKart. Exciting
    opportunity to develop and groom a unique skillset which intersects Technology, Product management and Marketing
    functions
  </p>

  <p><strong>Responsibilities:</strong></p>

  <p>Setup Analytics infrastructure</p>
  <ul>
    <li>Identification of OLTP datasources and ETL strategy </li>
    <li>Datawarehousing strategy and OLAP modeling          </li>
    <li>BI reporting platform setup and management          </li>
    <li>Organization setup and team management              </li>
  </ul>

  <p>Coordination with departments to define and execute analytics projects</p>
  <ul>
    <li>Marketing optimization               </li>
    <li>Consumer behavior and web analytics  </li>
    <li>Merchandizing and pricing automation </li>
    <li>Inventory automation                 </li>
    <li>Web personalization                  </li>
    <li>Social media analytics               </li>
  </ul>

  <p><strong>Skills/Experience:</strong></p>
  <ul>
    <li>5+ years of experience spanning product management, engineering / technology  and online marketing / analytics roles </li>
    <li>Experience with leading travel sites or eCommerce companies a big +                                                  </li>
    <li>Undergraduate from top tier engineering institute (IIT preferred)                                                    </li>
    <li>Very strong analytical skills                                                                                        </li>
    <li>Entrepreneurial spirit and extreme result orientation                                                                </li>
    <li>Ability to lead teams                                                                                                </li>
    <li>Good written, verbal and presentation skills                                                                         </li>
  </ul>
  <p><strong>Compensation:</strong></p>
  <ul>
    <li>Would be commensurate with experience</li>
    <li>Range between Rs 12 LPA to 18 LPA</li>
  </ul>
</div>

<div id="cs">
  <h3>Position: Customer Development Executive
  </h3>

  <p><strong>Responsibilities:</strong></p>
  <ul>
    <li>To answer customer queries related to products, health issues and the process of online ordering
      Internet and phone based counseling
    </li>
  </ul>
  <p><strong>Skills required:</strong></p>
  <ul>
    <li>Excellent English communication skills, both verbal and written</li>
    <li>Good knowledge of computers, especially internet</li>
    <li>Willingness to learn about health issues and products</li>
    <li>Positive attitude and customer oriented approach</li>
  </ul>
  <p><strong>Experience:</strong></p>
  <ul>
    <li>1-2 years of relevant experience in customer service</li>
  </ul>
  <p><strong>Compensation:</strong></p>
  <ul>
    <li>Rs 1.2 to 2 L per annum</li>
    <li>High performers would be promoted fast</li>
  </ul>
</div>


<div id="sales">
  <h3>Position: Direct Sales Manager</h3>

  <p>Seeking a result oriented driven individual to build direct sales channel for the company. The role provides fast pace learning environment for grooming general management skills.</p>
  <p><strong>Responsibilities:</strong></p>
  <p>Build, grow and manage direct sales channel, which includes</p>
  <ul>
    <li>Creating business plan for direct channel development and growth – team required, team organization and target setting  </li>
    <li>Strategy for driving growth – Lead generation, driving conversion and managing customer relationships                   </li>
    <li>Identification of adjoining growth opportunities                                                                        </li>
  </ul>
  <p>Managing a team of 15-20 to deliver targets</p>
  <p><strong>Skills/Experience:</strong></p>
  <ul>
    <li>MBA and undergraduate from top tier institution with good academic performance        </li>
    <li>2-3 years of work experience in entrepreneurial / consulting / analytical roles       </li>
    <li>Strategy and analytical skills                                                        </li>
    <li>Hunger to learn quickly and achieve fast paced growth in general management career    </li>
    <li>Entrepreneurial spirit and extreme result orientation                                 </li>
    <li>Ability to lead teams                                                                 </li>
  </ul>
  <p><strong>Compensation:</strong></p>
  <ul>
    <li>Between 9 LPA to 15 LPA</li>
    <li>High performers would be promoted fast</li>
  </ul>
</div>


<div id="design">
  <h3>Position: Graphic Design Lead</h3>

  <p><strong>Job description:</strong></p>
  <p>Healthkart is looking for a graphic design lead. Maybe two. Apart from raising the titanic, your job involves</p>
  <ul style="list-style:circle; padding-left:20px">
    <li>conceptualizing the overall look and feel and sometimes copy of all our marketing collaterals</li>
    <li>working with the copywriters and front end engineers to guide the design to fruition</li>
    <li>conceptualizing the display ads, print ads etc. based on key objectives as needed by the business/campaign</li>
    <li>building our retail presence with store design and below the line communications</li>
  </ul>
  <p><strong>Skills and experience:</strong></p>
  <ul style="list-style:circle; padding-left:20px">
    <li>Excellent understanding of typography, color theory and layouts</li>
    <li>Proficient with Photoshop, Illustrator</li>
    <li>Branding - a strong understanding of how copy and design go hand in hand. Setting the design guidelines for various media like website, print, banners etc.</li>
    <li>Ability to lead a team of designers and co-ordinate with engineers and copy writers to get the desired output</li>
    <li>Ability to improvise and be productive in a time and resource constrained environment</li>
    <li>We'd like someone who has worked with a design agency, preferably the younger ones</li>
    <li>We'd also like you to have graduated from some prestigious institution with a Visual Communications Degree</li>
  </ul>
  <p><strong>Experience:</strong></p>
  <ul style="list-style:circle; padding-left:20px">
    <li>2-5 years in design</li>
  </ul>
  <p><strong>Compensation:</strong></p>
  <ul style="list-style:circle; padding-left:20px">
    <li>As per industry standards.</li>
    <li>High performers would be promoted fast</li>
  </ul>
</div>

<div id="tech">

  <h3>Position: Lead/Senior/Junior Software Engineer(Java/J2EE)</h3>
  <p>
    <b>Responsibilities:</b>
  <ul style="list-style:circle; padding-left:20px">
    <li>We are in early stages of development of an extensive ecommerce platform which will have a consumer
      web store listing thousands of SKU's in the front end and support core business processes from
      procurement, sourcing, inventory management, customer support etc. in the backend. This is an exciting
      opportunity to join a small team of very smart developers and getting involved in a key technology role.
    </li>
  </ul>
  </p>

  <p>
    <b>Skills required:</b>
  <ul style="list-style:circle; padding-left:20px">
    <li>Language – Java; should be proficient in core java concepts</li>
    <li>Java Framework Technologies – Hibernate, Spring/Struts/Stripes</li>
    <li>DB Technologies: MySQL/Oracle; proficient in DB designing and writing queries</li>
    <li>Scripting Technologies: Javascript(jQuery, backbone.js etc.), Groovy</li>
    <li>Front end Technologies: HTML, CSS, JSP</li>
    <li>Comfortable in Linux, Tomcat and Apache</li>
    <li>Others - Solid problem solving skills, quick lerner and good team player</li>
    <li>Proven record of quality work as an individual and as a team member</li>
  </ul>
  </p>

  <p>
    <b>Experience:</b>
  <ul style="list-style:circle; padding-left:20px">
    <li>1-6 years of experience in Java web application development</li>
    <li>Should have worked in a significant capacity in the development stages of an internet based product
    </li>
    <li>Comfortable with the backend stack from business logic to database design</li>
  </ul>
  </p>

  <p>
    <b>Compensation:</b>
  <ul style="list-style:circle; padding-left:20px">
    <li>Would be commensurate with experience</li>
    <li>Range between Rs 5 LPA to 20 LPA. Will consider higher salary for exceptional candidates</li>
  </ul>
  </p>


  <hr>
  <br/>

  <h3>Position: UI/UX and Creative Lead</h3>


  <p><b>Job description </b>:<br/>
    HealthKart.com is in early stages of development of an extensive eCommerce platform which will have a consumer
    web store listing thousands of SKU's in the front end. We also have certain healthcare specific needs which
    need to be specially thought of and melded into the design. We believe that UI/UX and branding will make a
    significant impact long term and are looking forward to working on it in a planned fashion. The kind of work
    one can expect is as follows -
  <ul style="list-style:circle; padding-left:20px">
    <li>conceptualizing the overall look and feel, logo, navigation, colors and copy etc</li>
    <li>working with the copywriters and front end engineers to guide the design to fruition</li>
    <li>the overall branding. setting the tone for off site promotions so that they give the feel of the same
      brand. This includes look and feel, typography and copy.
    </li>
    <li>conceptualizing the display ads, print ads etc. based on key objectives as needed by the
      business/campaign.
    </li>
  </ul>
  </p>

  <p><b>Skills and experience </b>:<br/>
  <ul style="list-style:circle; padding-left:20px">
    <li>Must have had atleast 3-4 years experience in UI/UX design for the web</li>
    <li>Excellent understanding of typography, color theory and layouts</li>
    <li>Good understanding of HTML/CSS. (You will not necessarily need to code, but must have a strong
      understanding to know what's practical and what's not)
    </li>
    <li>Proficient with Photoshop, Illustrator/CorelDraw</li>
    <li>Branding - a strong understanding of how copy and design go hand in hand. Setting the design guidelines
      for various media like website, print, banners etc.
    </li>
    <li>Ability to lead a team of designers and co-ordinate with engineers and copy writers to get the desired
      output
    </li>
    <li>Ability to improvise and be productive in a time and resource constrained environment</li>
  </ul>
  <br/>
  </p>

  <p><b>Compensation </b>:<br/>
    6 - 10 LPA depending on experience and skills. Will consider higher salary for exceptional candidates.
  </p>

  <p><b>Job Localtion </b>:<br/>
    Gurgaon
  </p>

  <p><b>Pluses </b>:<br/>
    We're a young team of entrepreneurs. We're well funded. And we're in it for a long term to build a successful
    consumer brand. This will be a great opportunity for someone to come in at an early stage and contribute in a
    significant capacity to this vision.
  </p>

  <hr/>
  <br/>


  <%--<h3>Position: QA Engineer (Manual, Automation, Performace)</h3>

  <p>
    <b>Responsibilities:</b>
  <ul style="list-style:circle; padding-left:20px">
    <li>Software testing and quality assurance</li>
    <li>Adavaced analysis of comsumer behavior and features suggestions</li>
  </ul>
  </p>

  <p>
    <b>Skills required:</b>
  <ul style="list-style:circle; padding-left:20px">
    <li>Manual and Automation testing</li>
    <li>The position requires very good experience in performance testing of Java web based application</li>
    <li>Must have strong coding Skills on JavaScript, Java and UNIX OS - shell scripting or Python</li>
    <li>Good understanding on Web Application Development and Testing</li>
    <li>Prior experience in Testing (and more in the area of Performance Testing) and Debugging problems (Minimal
      2 year experience)
    </li>
    <li>Ability to Design small systems/frameworks will be an added advantage.</li>
  </ul>
  </p>

  <p>
    <b>Experience:</b>
  <ul style="list-style:circle; padding-left:20px">
    <li>3-5 years of experience in Java web application Testing and QA</li>
    <li>Should have worked in a significant capacity in the testing and QA stages of an internet based product
    </li>
  </ul>
  </p>

  <p>
    <b>Compensation:</b>
  <ul style="list-style:circle; padding-left:20px">
    <li>Would be commensurate with experience</li>
    <li>Range between Rs 5 LPA to 9 LPA. Will consider higher salary for exceptional candidates</li>
  </ul>
  </p>--%>


</div>

<div id="category">
  <h3>Position: Multi-channel Category Manager</h3>
  <p>
    Seeking a result oriented individual to drive one of the core categories of the platform, across online and offline
    distribution channels. The role will provide fast pace learning environment for grooming general management skills.
  </p>

  <p>
    <b>Responsibilities:</b>
  <ul style="list-style:circle; padding-left:20px">

  	<li>Manage P&L of one of the core categories on the platform, which includes     </li>
    <li> - Multi-channel coordination – online, offline and direct                      </li>
    <li> - Management of sales, growth, margins                                         </li>
    <li> - Procurement and vendor management                                            </li>
    <li> - Inventory Management                                                         </li>
    <li> - Merchandising                                                                </li>
    <li> - Promotions and Marketing coordination                                        </li>
    <li>Management of team for smooth operations                                     </li>

  </ul>
  </p>

  <p>
    <b>Skills/Experience:</b>
  <ul style="list-style:circle; padding-left:20px">
    <li>MBA and undergraduate from top tier institution with good academic performance       </li>
    <li>2-3 years of work experience in entrepreneurial / consulting / analytical roles      </li>
    <li>Comfort with technology / online space                                               </li>
    <li>Hunger to learn quickly and achieve fast paced growth in general management career   </li>
    <li>Entrepreneurial spirit and extreme result orientation                                </li>
    <li>Strategy and analytical skills                                                       </li>
    <li>Ability to lead teams                                                                </li>
    <li>Good written, verbal and presentation skills                                         </li>
  </ul>
  </p>

  <p>
    <b>Compensation:</b>
  <ul style="list-style:circle; padding-left:20px">
    <li>Would be commensurate with experience</li>
    <li>Range between Rs 9 LPA to 15 LPA</li>
    <li>Variable pay would be structured to give more opportunity for target based achievement</li>
  </ul>
  </p>

  <hr/>

  <h3>Position: Merchandising Manager</h3>
  <p>
    Seeking an analytically sharp individual who could manage online growth of core categories on the platform.
  </p>

  <p>
    <b>Responsibilities:</b>
  <ul style="list-style:circle; padding-left:20px">
    <li>Merchandizing and product placement to optimize conversions                  </li>
    <li>Pricing, promotion and marketing strategy of products                        </li>
    <li>Promotion and marketing execution via coordination with marketing department </li>
    <li>Cateloging and content of catalogued products                                </li>
  </ul>
  </p>

  <p>
    <b>Skills/Experience:</b>
  <ul style="list-style:circle; padding-left:20px">
    <li>Undergraduate from top tier engineering institute (IIT preferred) </li>
    <li>Strong analytical skills and data orientation                     </li>
    <li>Comfort with technology / online space                            </li>
    <li>Good team skills                                                  </li>
    <li>Good written, verbal and presentation skills                      </li>
  </ul>
  </p>

  <p>
    <b>Compensation:</b>
  <ul style="list-style:circle; padding-left:20px">
    <li>Would be commensurate with experience</li>
    <li>Range between Rs 7 LPA to 10 LPA</li>
  </ul>
  </p>


</div>
</div>
</div>

</div>


<div class="cl"></div>
<!-- Container Ends-->
<!-- AddThis Button BEGIN -->
<div class="addthis_toolbox addthis_floating_style addthis_counter_style" style="right:10px;top:50px;">
  <a class="addthis_button_facebook_like" fb:like:layout="box_count"></a>
  <a class="addthis_button_tweet" tw:count="vertical"></a>
  <a class="addthis_button_google_plusone" g:plusone:size="tall"></a>
  <a class="addthis_counter"></a>
</div>
<script type="text/javascript"
        src="http://s7.addthis.com/js/300/addthis_widget.js#pubid=ra-509261de45f6e306"></script>
<!-- AddThis Button END -->
</s:layout-component>


</s:layout-render>

