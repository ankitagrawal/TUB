<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<title>
      sports-nutrition | HealthKart.com
    </title>
<c:set var="topLevelCategory" value="sports-nutrition"/>
    <link href="style.css" rel="stylesheet" type="text/css"/>

<div id="container">
<div class="greenBx" id="rMenu">
    <div class="yellowBx" id="ttf"><a href="javascript:void(0)" onclick="beforeCls()" class="closeBtn">
        &nbsp;</a>

        <div class="pad1Form">
            <form id="sendCouponForm">
                <div class="nRow">
                    <h3>Fill the form below and our Experts will get back to you soon</h3>

                    <p>Enter your name</p>

                    <p><input type="text" name="name" class="bgFormInputs" id="name"/></p>

                    <p>Phone Number</p>

                    <p><input type="text" name="mobile" class="bgFormInputs" id="phone"/></p>

                    <p>Email address</p>

                    <p><input type="text" name="email" class="bgFormInputs" id="email"/></p>

                    <div class="nRow">
                        <div class="bgFormCBArea"><input name="" type="checkbox" value="" id="subscribe"/></div>
                        <div class="bgFormCBMsgArea">I would like to receive updates/ new offers from
                            HealthKart.com in future through Phone/SMS/E-Mails.
                        </div>
                        <div class="cl"></div>
                    </div>
                    <p style="margin-top:5px;">
                        <a href="javascript:void(0)" id="sendCouponLink">
                            <img src="images/submit.jpg" class="submitBtnRed" alt="send coupon"> </a>

                            <%--<button type="submit" name="getContact" onclick="return _validation()" class="submitBtnRed">--%>
                            <%--<img src="images/submit.jpg" alt="Submit">--%>
                            <%--</button>--%>
                        <!--<input type="submit"  name="getContact" onclick="return _validation()">-->
                        <!--<img src="images/submit.jpg" alt="Submit" title="Submit" id="submit"/>-->
                        <!--</input>-->
                    </p>
                </div>
                <div class="nRow">&nbsp;</div>
            </form>
        </div>
    </div>
    <a href="javascript:void(0)" onclick="fixT()" class="one">&nbsp;</a>
</div>
<script type="text/javascript" src="<hk:vhostJs/>/js/jquery-1.6.2.min.js"></script>
    <script type="text/javascript" src="<hk:vhostJs/>/js/jquery.hkCommonPlugins.js"></script>
    <script type="text/javascript" src="<hk:vhostJs/>/js/jquery.bxSlider.min.js"></script>
    <script type="text/javascript" src="<hk:vhostJs/>/scripts/field-validation.js"></script>
<script type="text/javascript">
    $(document).ready(function () {
        $('#sendCouponLink').click(function () {
            var name = document.getElementById("name").value;
            var phoneNo = document.getElementById("phone").value;
            var email = document.getElementById("email").value;
            if (name == null || name == "" || phoneNo == null || phoneNo == "" || email == null || email == "") {
                alert("Please Enter All Fields!!");
                return false;
            } else if (isNaN(phoneNo)) {
                alert("Please Enter Phone No. in correct Format");
                return false;
            } else if (phoneNo.length != 10) {
                alert("Please Enter 10 digit Phone No");
                return false;
            }
            var subscribe = $('#subscribe').is(':checked');
            var params = {};
            params['srcUrl'] = document.location.href;
            params['topLevelCategory'] = "${topLevelCategory}";
            params['name'] = name;
            params['mobile'] = phoneNo;
            params['email'] = email;
            params['subscribe'] = subscribe;
            $.ajax({
                        type: "POST",
                        data: params,
                        dataType: "json",
                        url: "${pageContext.request.contextPath}/core/user/RequestCallback.action?getContact=",
                        success: function (response) {
                            alert(response.message);
                            $('.greenBx').hide();
                        },
                        error: function onError() {
                            alert('Could not save Your details please try again');
                        }
                    }

            );

        });
    });
</script>

<div class="nRow pageHeadImg1"><img src="images/the-three-pillars-of-bodybuilding.jpg"
                                    alt="THE 3 PILLARS OF BODYBUILDING"/></div>
<div class="nRow pageHeadImg2"><img src="images/you-should-never-compromise-on.jpg"
                                    alt="You shouldnever compromise on"/></div>
<div class="nRow">
    <h1>One fine day, you decide that enough is enough. You need to get into shape and fast.<span> Fast and how? You go to the gym and start with whatever you can get your hands on. Fact is, you end up doing far more harm than good to yourself. Ladies and gentlemen, here are three tenets of exercising that you should never ever take lightly</span>
    </h1>
</div>
<div class="nRow mainHead1">
    <p><img src="images/exercise.jpg" alt="EXERCISE" title="EXERCISE"/></p>

    <p><img src="images/exercise-heading1.jpg"/></p>
</div>
<div class="nRow">
    <div class="leftBar">
        <p class="paraGp1">If everything that you do at the gym produces no results, then in all <br/>probability,
            you are the common <br/>denominator. Consider it a huge <br/>mismatch between your set of goals
            and<br/> the kind of exercise you are undertaking-<br/> they are perfect on their own but just
            <br/>don’t complement each other.</p>

        <p>Both strength training and cardio yield<br/>excellent results when your goal is as<br/>simple as
            weight loss.</p>

        <p>But with levels going up from beginner to<br/> even as little as an advanced-beginner<br/>and
            intermediate, expert guidance<br/>becomes a necessity. Because on your<br/> own, you just can’t
            tell where the finish<br/> line is or if you even came close to it.</p>
    </div>
    <div class="centerBar">
        <p>Why do you need expert guidance for exercise?</p>

        <p>Weight loss leads to sagging skin. <br/>Unguided exercise regimes target only<br/>the broad goal
            of weight loss but miss on<br/>the target of keeping the skin from<br/> sagging.</p>

        <p>Toning requires a reasonable amount of strength and resistance training. <br/>Needless to say, a
            freehand approach will <br/>only leave you either using more weight<br/>than you can handle or
            less than you can<br/>actually pull off. In either case, your<br/> muscles pay dearly.</p>

        <p>Bulking up doesn’t happen for anyone without the unique, body-specific <br/>exercise patterns.
            Measurements like<br/>BMI, body fat ratio and individual <br/>metabolic rates differ for each
            person.</p>
    </div>
    <div class="rightBar">
        <p class="paraGp1">And it is these that direct which exercises<br/>you must incorporate and what
            level.</p>

        <p class="paraGp1">Expert guidance helps you avoid common mistakes like overdoing cardio for weight
            loss. That not only leads to muscle loss <br/>but also slows down weight loss. You <br/>must
            have a moderation level for any exercise you do. It might look easy but <br/>you can’t get there
            all by yourself by <br/>making such mistakes and ultimately hampering the very goal you started
            with.</p>

        <p>Unguided workout regimes lead to:</p>
        <ul class="listOne">
            <li>-sagged skin from excess weight loss</li>
            <li>-injury risk from heavy lifts</li>
            <li>-disproportionate development</li>
            <li>-overtraining</li>
            <li>-undertraining</li>
        </ul>
    </div>
    <div class="cl"></div>
</div>
<div class="nRow mainHead1">
    <p><img src="images/whole-food.jpg" alt="WHOLE FOOD" title="WHOLE FOOD"/></p>

    <p><img src="images/whole-food-heading2.jpg"/></p>
</div>
<div class="nRow">
    <div class="leftBar">
        <div class="bananaPic"><img src="images/bananas.png" alt="Bananas"/></div>
        <div class="broccoliPic"><img src="images/broccoli.png" alt="Broccoli"/></div>
        <pre>After sweating buckets in the<br/>gym, whole foods are<br/>the recovery vials to go for. The<br/>jazzy, processed food shelves<br/>may keep your eyes hooked but<br/>if you can’t fight that gaze, you should<br/>just as well toss your athletic goals.<br/>With intense workout sessions comes the<br/>right kind of tone. But it also brings along<br/>muscular injury. The right food surprises<br/>your body with the nourishment it needs<br/>                                     to recover from<br/>                                     the workout<br/>                                     damage.<br/>                                     If your diet isn’t<br/>                                     right, you are<br/>never going to get any closer to your<br/>dream physique. <br/>To start on the right track, hit your,<br/> grocery store and tick all the checkboxes<br/>on the bodybuilder’s grocery list:<br/>Oatmeal. Every meal of oats contains 150<br/>calories and only 3 grams of fat with slow<br/>burning complex carbs and fiber.<br/></pre>
        <p>Bok choy. With your each cup of Bok</p>
    </div>
    <div class="centerBar">
        <div class="oatMealPic"><img src="images/oat-meal.png" alt="Oat Meal"/></div>
        <pre>                    Choy, you consume absolutely<br/>                    no fat and only 9 calories with<br/>                    1.05 g of protein.<br/>                    <strong>Bananas.</strong> One medium sized<br/>                    banana is enriched with 105<br/>calories, 27 grams of carbs, very low<br/>saturated fat and high source of dietary<br/>fiber- an instant energy booster. <br/><strong>Tofu.</strong> Half a cup of raw tofu contains 10.1<br/>grams of protein, 5 grams of fat with zero<br/>cholesterol.<br/><strong>Mutton.</strong> With relatively higher level of<br/>iron than beef, pork, lamb and chicken,<br/>mutton has 3.2 mg per 3 oz. higher<br/>potassium content and essential amino acids.<br/><strong>Flaxseeds.</strong> This is one of the best<br/>sources of Omega-3 essential fatty acid,<br/>where one spoon of seeds contains 8 g of<br/>good fat.<br/><strong>Broccoli.</strong> Every 100g contains just 34<br/>calories but is rich in dietary fiber,<br/>vitamins and<br/>anti-oxidants.<br/>Expert care for<br/>exercises is alright.<br/>But</pre>
    </div>
    <div class="rightBar">
        <div class="muttonPic"><img src="images/mutton.png" alt="Mutton"/></div>
        <pre>why do you need specialist advice<br/>for foods? <br/>It isn’t as easy as picking up the right<br/>foods and gobbling them down when<br/>hunger rings. It would be if you were<br/>aiming at keeping healthy. But with<br/>stricter goals<br/>like getting<br/>athletic and<br/>turning yourself<br/>into a Hulk, it goes beyond that.<br/>Factors like your protein consumption to<br/>workout ratio, which foods to consume at<br/>what time of the day, when to eat<br/>high-glycemic foods and when to avoid<br/>them, should you consume fat for your<br/>goal or should you stick to a low-carb<br/>grocery list plus a ton of other questions <br/>need trainer guidance. With much <br/>hubbub around these, it is difficult to get <br/>genuine information. And even if you do, <br/>         it will often leave you confused and <br/>        your muscles vulnerable to the <br/>       consequences of misjudgment.</pre>
    </div>
    <div class="cl"></div>
</div>
<div class="nRow mainHead1">
    <p><img src="images/supplements.jpg" alt="Supplements" title="Supplements"/></p>

    <p><img src="images/supplements-heading1.jpg" alt="Processed food might seem the right thing to gorge upon, especially when you
    are short of time. But are they any match to the whole foods? Not really."/></p>
</div>
<div class="nRow">
    <div class="leftBar">
        <div class="supplementPic1"><img src="images/muscleblaze-whey-protein.png"
                                         alt="MuscleBlase Whey Protein"/></div>
        <pre>Supplements are by far the best thing to<br/>have happened to the court of<br/>bodybuilding. Not only are they engines<br/>of optimum nourishment, they also add<br/>ease of use over whole foods.</pre>
        <pre>As you move toward your goal, <br/>supplements play an essential role in <br/>your diet regime. There are supplements<br/>divided according to their usage and<br/>results; pre-workout supplements,<br/>post-workout supplements, supplements<br/>for recovery etc.</pre>
                    <pre>                                 These 6 essential<br/>                                 supplements will<br/>                                 add fuel to your<br/>                                 fire<br/>                                <strong>Whey
                        Protein.</strong><br/>                                With 80% of protein <br/>                                and all essential amino <br/>                                acids, it is the purest<br/>form of protein for building lean muscle<br/>mass and muscle recovery<br/>Amino acids. Boost protein synthesis and <br/>decrease protein breakdown, resulting in <br/>muscle gain <br/><strong>Creatine. </strong>The water retaining ability of <br/>creatine saturates your muscles, giving <br/>them a fuller appearance <br/><strong>Casein
                        protein.</strong> A slow release protein<br/>ideal for night time consumption, its high <br/>calcium content benefits total fat loss <br/>Beta Alanine. A pre-workout <br/>supplement that lowers muscle pH, <br/>leading to reduced muscle contraction</pre>
    </div>

    <div class="centerBar">
        <div class="supplementPic2"><img src="images/super-amino.png" alt="Dymatize Super Amino"/></div>
        <div class="supplementPic3"><img src="images/qnt-l-glutamin.png" alt="QNT L Glutamin"/></div>
        <pre>and hence, better<br/>performance <br/><strong>Glutamine.</strong> By its <br/>ability to minimize <br/>muscle breakdown <br/>and improve protein <br/>synthesis, it restores <br/>depleted glutamine<br/>level in the body during workout.</pre>
        <pre>But no matter what your goal, you need<br/>the secret triad to engineer the look for <br/>you. <br/>Each individual’s body functions uniquely <br/>and each element of the triumvirate <br/>needs optimization accordingly. One<br/>exercise pattern and diet plan may work<br/>wonders for your gym buddy but it won’t <br/>necessarily be as useful for everyone else.</pre>
        <p>Why add supplements to your diet?</p>
        <ul class="listOne">
            <li>-Ease of use</li>
            <li>-Convenient over 10 egg whites or <br/>
                &nbsp;600 gms of meat
            </li>
            <li>-Goal specific</li>
            <li>-Better results</li>
            <li>-Accelerate your progress over whole foods</li>
        </ul>
        <pre>As against whole foods, they are a lot <br/>convenient and <br/>highly goal specific <br/>but getting the <br/>right supplement is <br/>very critical.</pre>
    </div>

    <div class="rightBar">
        <div class="supplementPic4"><img src="images/weider-pure-creatine.png" alt="Weider Pure Creatine"/>
        </div>
        <div class="supplementPic5"><img src="images/muscletech-nitro-tech.png"
                                         alt="Muscletech Nitro Tech"/></div>
        <pre>Is the supplement <br/>you choose suitable <br/>for your body? Is it <br/>the right one for <br/>you? When should it <br/>be taken for best <br/>results? Your <br/>seemingly ample information might <br/>never be sufficient for that lean, <br/>impeccable physique. With all the money <br/>you put in for the supplements, you sure<br/>don’t want to find yourself baffled at the <br/>end of the day with no results to cherish.</pre>
        <pre>And it doesn’t end<br/> there. There is more <br/>to it and more than <br/>you can devise on <br/>your own. Without <br/>the ideal training <br/>that caters to your<br/>individual needs, adequate diet and<br/>the exact supplement, the athletic road<br/>will be the  one never travelled by you.<br/>So pick the right ingredients for your<br/>bodybuilding diary- the accurate amount <br/>and detailed tactics ranging across<br/>exercise routines, diet plans comprising <br/>whole foods and fitting supplements- and <br/>aim for your target.<br/>And once you do that, for the nuts and<br/>bolts that will get you the chisel, call our <br/>bodybuilding and nutrition experts to<br/>apply this secret today! Or better still;<br/>approach them for your queries. We are<br/>sure we generated some.</pre>
    </div>
    <div class="cl"></div>
</div>
<div class="nRow">&nbsp;</div>
<div class="backToHK">
    <a href="http://www.healthkart.com">HealthKart Home</a>
</div>
<div class="cl"></div>
</div>
<script src="js/srcoll-menu.js"></script>
