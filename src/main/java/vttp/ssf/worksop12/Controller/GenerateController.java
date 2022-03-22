package vttp.ssf.worksop12.Controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import vttp.ssf.worksop12.Exception.RandomNumberException;
import vttp.ssf.worksop12.Model.Generate;

@Controller
public class GenerateController {
    private Logger logger = LoggerFactory.getLogger(GenerateController.class);
    

    //end point to foward to a generate.html page
    //root of the path is set therefore when user access this
    // web app it will always default to this method

    @GetMapping("/")
    public String showGenerateForm(Model model){
        // instantiate the Generate class wehere it represent the form
        //of the generate html page
        Generate generate = new Generate();
        // set the generate class into the page scope of the generate html
        model.addAttribute("generateObj", generate);
        // foward this endpoint to the page "generate.html"
        return "generatePage";
    }

    // this end point is to handle the form submission
    @PostMapping("/generate")
    public String generateNumbers(@ModelAttribute Generate generate, Model model){
        try{
            logger.info("From the form" + generate.getNumberVal());
            // capture the number of times that the user input on
            // the random image page
            int numberRandomNumbers = generate.getNumberVal();
            //check if the threshold of how many numbers can the user generate
            if (numberRandomNumbers > 10){
                throw new RandomNumberException();
            }

            // initalise all the relevant number images upfront
            // download the images from the internet and place it under the
            //static/images directory
            String[] imgNumbers = {
                "1.png", "2.png", "3.png", "4.png", "5.png",
                "6.png", "7.png", "8.png", "9.png", "10.png"
            };

            //Create a list of string that holds the selected images from the
            // the random generate numbers
            List<String> selectedImg = new ArrayList<String>();
            //use the randomizer that is out of the box from Java
            Random randNum = new Random();
            // use linkedHashset to house the generate numbers
            // in order to avoid duplicate
            Set<Integer> uniqueGeneratedRandNumSet = new LinkedHashSet<Integer>();
            // loop through every single number which is generated from the Random class
            // and place it into the hash linkedHashset
            while(uniqueGeneratedRandNumSet.size() < numberRandomNumbers){
                Integer resultOfRandNumbers = randNum.nextInt(generate.getNumberVal() +1);
                uniqueGeneratedRandNumSet.add(resultOfRandNumbers);
            }
            // once the above process is done we have a list of numbers 
            // gurantee is unique across the required times that the user input
            Iterator<Integer> it = uniqueGeneratedRandNumSet.iterator();
            Integer currentElem = null;
            // loop against the linkedhash set
            // so we can map the images from the array we declare earlier on
            while(it.hasNext()){
                currentElem = it.next();
                logger.info("currentElem > " + currentElem);
                // the stored random generated number of this index value
                // might not be the same as the image array
                // e.g. if the number that is generated is 3
                // then is the 4th position of the selected image array
                // that will return us "4.png"
                selectedImg.add(imgNumbers[currentElem.intValue()]);
             }
            // lastly we have to bind the selected images array 
            // into the model object and allow the view to render it 
            model.addAttribute("randNumsResult", selectedImg.toArray());
            model.addAttribute("numInputByUser", numberRandomNumbers);
        } catch(RandomNumberException e){
            // Once we catch the above error 
            // the program will populate an error message on the model
            // so it allows the error page to take in a message to show
            // directly to the user
            model.addAttribute("errorMessage", "Number entered is more than 10!");
            // forward to the error.html under the templates directory
            return "error";
        }
        // forward to the result pages to show a list of random
        // generated from the above flow.x
        return "result";
    }
    
}
