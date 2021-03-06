package com.jingwei.mobile.match;

import java.util.List;

import com.jingwei.mobile.card.Card;
import com.jingwei.mobile.card.CardBean;
import com.jingwei.mobile.card.ICardHeaders;
import com.jingwei.mobile.result.AddressResult;
import com.jingwei.mobile.result.NameResult;
import com.jingwei.mobile.result.ResultBase;
import com.jingwei.mobile.util.Levenshtein;
import com.jingwei.mobile.util.Utility;

public class AddressMatcher extends MatchBase {

	@Override
	public AddressResult Match(CardBean cardBean, Card card) {

		AddressResult result = new AddressResult();
		
		String address = cardBean.getAddress();

		if(address == null || address == ""){
			return result;
		}
		
		result.Count = 1;
		
		/**
		 * Start matching, recursively compare all the fields in ocr result.
		 * Find if there is any wrong field mapping 
		 */
		
		String expected = address;
		expected = Utility.TrimNConvert(expected);
		
		List<String> processedActualValueList = Utility.TrimNConvert(card.getValuesList());
		String actual = this.getMostLikeStr(expected, processedActualValueList);
		
		int distance = Levenshtein.Compare(expected, actual);
		if(distance == expected.length()){
			// means not found similar field in ocr result
			// failed one, ignore this match
		}else{
			// ELSE means found a similar one in ocr result
			// To see if the attribute is right:
			int indexOfActual = processedActualValueList.indexOf(actual);
			int attribOfActual = card.getAttribList().get(indexOfActual);
			if(attribOfActual != ICardHeaders.NAMECARD_ADDRESS){
				result.filedMismatchCount++;
			}
			
			// to see if bingo
			result.Distance += distance;
			result.Length += expected.length();
			
			int len = expected.length();
			if( (len - distance) / len >= ADDRESSMATCHRATE){
				result.Bingo++;
			}
		}
		
		return result;
	}

}
