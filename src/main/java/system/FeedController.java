package system;

import java.util.List;
import java.util.Map.Entry;

import com.google.common.collect.Lists;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import system.access.ChannelAnalytics;
import system.shared.Video;

@Log4j2
@RestController
public class FeedController
{
    @Autowired
    private LastFeedContainer lastFeedContainer;

    @Autowired
    private WordsFrequencyAnalyser wordsFrequencyAnalyser;

    @Autowired
    private ChannelAnalytics channelAnalytics;

    @RequestMapping("/trends")
    public ResponseEntity<List<Video>> getTrends()
    {
       return ResponseEntity.ok(lastFeedContainer.getAllFeed());
    }

    @RequestMapping("/popularWords")
    public ResponseEntity<List<Entry<String, Integer>>> getPopularWords(@RequestParam(value = "limit", defaultValue = "100") long limit)
    {
        return ResponseEntity.ok(wordsFrequencyAnalyser.getPopularWords(limit));
    }

    @RequestMapping("/topByHoursCount")
    public ResponseEntity<List<Entry<String, Integer>>> getTopByHoursCount()
    {
       return ResponseEntity.ok(channelAnalytics.getTopChannelsByHoursInTrends());
    }

    @RequestMapping("/topByVideosCount")
    public ResponseEntity<List<Entry<String, Integer>>> getTopByVideosCount()
    {
       return ResponseEntity.ok(channelAnalytics.getTopChannelsByCountUniqueVideoInTrends());
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity exceptionsHandler(Exception e)
    {
        log.error("Error", e);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

}
